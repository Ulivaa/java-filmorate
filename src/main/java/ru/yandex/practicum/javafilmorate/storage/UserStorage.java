package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void save(User user);

    Collection<User> returnAllUsers();
}
