package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.FriendshipStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {
    private final String isConfirmedQuery = "Select * from Friendships where user_id = ? AND friend_id = ?";
    private final String saveFriendshipUserQuery = "insert into Friendships (user_id, friend_id, is_confirmed) values (?, ?, ?)";
    private final String updateConfirmFriendQuery = "update Friendships set is_confirmed = ? where user_id = ? and friend_id = ?";
    private final String hasFriendshipQuery = "Select * from Friendships where user_id = ? AND friend_id = ?";
    private final String hasConfirmFriendshipQuery = "Select * from Friendships where user_id = ? AND friend_id = ?";
    private final String deleteConfirmForFriend = "update Friendships set is_confirmed = ? where user_id = ? and friend_id = ?";
    private final String deleteFriendship = "DELETE FROM Friendships where user_id = ? AND friend_id = ?";
    private final String getUserFriendsQuery = "Select * from Users u join Friendships f on u.user_id = f.friend_id where f.user_id = ? ";


    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;


    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    @Override
    public void save(int user_id, int friend_id) {
        /*Проверка на существование запроса на дружбу*/
        if (jdbcTemplate.queryForRowSet(isConfirmedQuery, friend_id, user_id)
                .next()) {
            /* Добавление подтвержденной дружбы у обоих */
            jdbcTemplate.update(saveFriendshipUserQuery,
                    user_id,
                    friend_id,
                    true);
            jdbcTemplate.update(updateConfirmFriendQuery,
                    true,
                    friend_id,
                    user_id);
        } else {
            /*Добавление запроса на дружбу*/
            jdbcTemplate.update(saveFriendshipUserQuery,
                    user_id,
                    friend_id,
                    false);
        }
        eventStorage.save(1 , user_id , LocalDateTime.now() , "FRIEND" , "ADD");
    }

    @Override
    public void delete(int user_id, int friend_id) {
        /* Проверка на существование дружбы*/
        if (jdbcTemplate.queryForRowSet(hasFriendshipQuery, user_id, friend_id)
                .next()) {
            /* Проверка на сущестование подтверждения */
            if (jdbcTemplate.queryForRowSet(hasConfirmFriendshipQuery, friend_id, user_id)
                    .next()) {
                /* Удаление подтверждения дружбы у бывшего друга */
                jdbcTemplate.update(deleteConfirmForFriend,
                        false,
                        friend_id,
                        user_id);
            }
            /* Удаление дружбы */
            jdbcTemplate.update(deleteFriendship,
                    user_id,
                    friend_id);
        }
        eventStorage.save(1 , user_id , LocalDateTime.now() , "FRIEND" , "REMOVE");
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return jdbcTemplate.query(getUserFriendsQuery, (rs, rowNum) -> UserDbStorage.makeUser(rs, rowNum), id);
    }

    @Override
    public Collection<User> getCommonFriends(Integer user_id, Integer user2_id) {

        return getFriends(user_id).stream()
                .distinct()
                .filter(o -> getFriends(user2_id).contains(o))
                .collect(Collectors.toSet());
    }
}
