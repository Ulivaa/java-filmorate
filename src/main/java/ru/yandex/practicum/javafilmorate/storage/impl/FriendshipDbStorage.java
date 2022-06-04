package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FriendshipStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Repository
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;


    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int user_id, int friend_id) {
        /*Проверка на существование запроса на дружбу*/
        String sql_isConfirm = "Select * from Friendships where user_id = ? AND friend_id = ?";
        if (jdbcTemplate.queryForRowSet(sql_isConfirm, friend_id, user_id)
                .next()) {
            /* Добавление подтвержденной дружбы у обоих */
            String sqlQuery_user = "insert into Friendships (user_id, friend_id, is_confirmed) values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery_user,
                    user_id,
                    friend_id,
                    true);
            String sqlQuery_friend = "update Friendships set is_confirmed = ? where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery_friend,
                    true,
                    friend_id,
                    user_id);
        } else {
            /*Добавление запроса на дружбу*/
            String sqlQuery = "insert into Friendships (user_id, friend_id, is_confirmed) values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    user_id,
                    friend_id,
                    false);
        }

    }

    @Override
    public void delete(int user_id, int friend_id) {
        /* Проверка на существование дружбы*/
        String sql_isFriendship = "Select * from Friendships where user_id = ? AND friend_id = ?";
        if (jdbcTemplate.queryForRowSet(sql_isFriendship, user_id, friend_id)
                .next()) {
            /* Проверка на сущестование подтверждения */
            String sql_isConfirm = "Select * from Friendships where user_id = ? AND friend_id = ?";
            if (jdbcTemplate.queryForRowSet(sql_isConfirm, friend_id, user_id)
                    .next()) {
                /* Удаление подтверждения дружбы у бывшего друга */
                String sqlQuery_friend = "update Friendships set is_confirmed = ? where user_id = ? and friend_id = ?";
                jdbcTemplate.update(sqlQuery_friend,
                        false,
                        friend_id,
                        user_id);
            }
            /* Удаление дружбы */
            String sqlQuery_user = "DELETE FROM Friendships where user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlQuery_user,
                    user_id,
                    friend_id);
        }
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        String sql_friends = "Select * from Users u join Friendships f on u.user_id = f.friend_id where f.user_id = ? ";

        return jdbcTemplate.query(sql_friends, (rs, rowNum) -> UserDbStorage.makeUser(rs, rowNum), id);
    }

    @Override
    public Collection<User> getCommonFriends(Integer user_id, Integer user2_id) {

        return getFriends(user_id).stream()
                .distinct()
                .filter(o -> getFriends(user2_id).contains(o))
                .collect(Collectors.toSet());
    }
}
