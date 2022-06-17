package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {
    void save(int userId, int friendId);

    void delete(int userId, int friendId);

    Collection<User> getFriends(Integer id);

    Collection<User> getCommonFriends(Integer userId, Integer user2Id);
}
