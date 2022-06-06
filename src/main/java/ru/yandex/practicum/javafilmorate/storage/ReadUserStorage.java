package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Optional;

@Repository
public interface ReadUserStorage {

     Optional<User> findUserById(int id);

     Optional<User> findUserByEmail(String email);
}
