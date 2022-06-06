package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {
    void save(int user_id, int friend_id);

    void delete(int user_id, int friend_id);

    Collection<User> getFriends(Integer id);

    Collection<User> getCommonFriends(Integer user_id, Integer user2_id);
}
