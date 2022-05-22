package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.dto.UserDto;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
       return userService.updateUser(user);
    }

    @GetMapping("/users")
    public Collection<User> returnAllUsers() {
        return userService.returnAllUsers();
    }

    @GetMapping("/users/{id}")
    public User returnUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addUserFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addUserFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeUserFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeUserFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> returnUserFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> returnCommonUserFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonUserFriends(id, otherId);
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