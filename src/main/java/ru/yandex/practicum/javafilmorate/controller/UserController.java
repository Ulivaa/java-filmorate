package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.dto.UserDto;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public void createUser(@RequestBody UserDto userDto) {
        User user = userDto.mapToUser(userDto);
        if (user.getLogin().contains(" ") || !user.getEmail().contains("@")) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        users.put(user.getId(), user);
        log.info("Добавлен объект {}", user.getLogin());
    }

    @PutMapping("/users")
    public void updateUser(@RequestBody User user) {
        if (user.getLogin().contains(" ") || !user.getEmail().contains("@")) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        users.put(user.getId(), user);
        log.info("Обновлен объект {}", user.getLogin());
    }

    @GetMapping("/users")
    public Collection<User> returnAllUsers() {
        return users.values();
    }
}

//пример
//{
//        "id": 1,
//        "login": "login",
//        "email": "email",
//        "name": "name",
//        "birthday": "1996-04-16"
//    }