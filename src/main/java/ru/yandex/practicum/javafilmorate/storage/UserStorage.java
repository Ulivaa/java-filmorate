package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    int save(User user);

    void delete(Integer user_id);

    void update(User user);

    Collection<User> returnAllUsers();
}
