package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    void save(User user);

    Optional<User> findUserById(int id);

    Optional<User> findUserByEmail(String email);

    void update(User user);

    Collection<User> returnAllUsers();
}
