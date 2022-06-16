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
    public void save(int userId, int friendId) {
        /*Проверка на существование запроса на дружбу*/
        if (jdbcTemplate.queryForRowSet(isConfirmedQuery, friendId, userId)
                .next()) {
            /* Добавление подтвержденной дружбы у обоих */
            jdbcTemplate.update(saveFriendshipUserQuery,
                    userId,
                    friendId,
                    true);
            jdbcTemplate.update(updateConfirmFriendQuery,
                    true,
                    friendId,
                    userId);
        } else {
            /*Добавление запроса на дружбу*/
            jdbcTemplate.update(saveFriendshipUserQuery,
                    userId,
                    friendId,
                    false);
        }
        eventStorage.save(friendId, userId, LocalDateTime.now(), "FRIEND", "ADD");
    }

    @Override
    public void delete(int userId, int friendId) {
        /* Проверка на существование дружбы*/
        if (jdbcTemplate.queryForRowSet(hasFriendshipQuery, userId, friendId)
                .next()) {
            /* Проверка на сущестование подтверждения */
            if (jdbcTemplate.queryForRowSet(hasConfirmFriendshipQuery, friendId, userId)
                    .next()) {
                /* Удаление подтверждения дружбы у бывшего друга */
                jdbcTemplate.update(deleteConfirmForFriend,
                        false,
                        friendId,
                        userId);
            }
            /* Удаление дружбы */
            jdbcTemplate.update(deleteFriendship,
                    userId,
                    friendId);
        }
        eventStorage.save(1, userId, LocalDateTime.now(), "FRIEND", "REMOVE");
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return jdbcTemplate.query(getUserFriendsQuery, (rs, rowNum) -> UserDbStorage.makeUser(rs, rowNum), id);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer user2Id) {

        return getFriends(userId).stream()
                .distinct()
                .filter(o -> getFriends(user2Id).contains(o))
                .collect(Collectors.toSet());
    }
}
