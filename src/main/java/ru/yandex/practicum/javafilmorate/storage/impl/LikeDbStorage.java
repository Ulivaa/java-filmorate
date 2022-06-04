package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.storage.LikeStorage;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int film_id, int user_id) {
        String sqlQuery = "insert into likes(film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                film_id,
                user_id);
    }
    @Override
    public void delete(int film_id, int user_id) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery,
                film_id,
                user_id);
    }
}
