package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.storage.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class LikeDbStorage implements LikeStorage {
    private final String saveLike = "insert into likes(film_id, user_id) values (?, ?)";
    private final String deleteLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private final String allForUserQuery = "SELECT film_id FROM likes WHERE user_id = ?";
    private final String allExceptUserQuery = "SELECT * FROM likes WHERE user_id <> ?";

    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    @Override
    public void save(int film_id, int user_id) {
        jdbcTemplate.update(saveLike,
                film_id,
                user_id);
        eventStorage.save(1 , user_id , LocalDateTime.now() , "LIKE" , "ADD");
    }

    @Override
    public void delete(int film_id, int user_id) {
        jdbcTemplate.update(deleteLike,
                film_id,
                user_id);
        eventStorage.save(1 , user_id , LocalDateTime.now() , "LIKE" , "REMOVE");
    }

    @Override
    public Set<Integer> allForUser(int userId) {
        return new HashSet<>(jdbcTemplate.query(allForUserQuery, (rs, i) -> rs.getInt("film_id"), userId));
    }

    @Override
    public Map<Integer, Set<Integer>> allExceptUser(int userId) {
        var map = new HashMap<Integer, Set<Integer>>();
        jdbcTemplate.query(allExceptUserQuery, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                var userId = rs.getInt("user_id");
                var film_id = rs.getInt("film_id");
                if (!map.containsKey(userId)) {
                    map.put(userId, new HashSet<>());
                }
                map.get(userId).add(film_id);
            }
        }, userId);
        return map;
    }

}
