package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public Collection<User> returnAllUsers() {
        return users.values();
    }
}
