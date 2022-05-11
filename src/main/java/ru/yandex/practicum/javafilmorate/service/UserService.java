package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dto.UserDto;
import ru.yandex.practicum.javafilmorate.exception.UserNotFoundException;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(UserDto userDto) {
        User user = userDto.mapToUser(userDto);
        if (validateDate(user)) {

            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        userStorage.save(user);
        log.info("Добавлен объект {}", user.getLogin());
        return findUserById(user.getId());
    }

    public void updateUser(User user) {
        if (validateDate(user)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        userStorage.save(user);
        log.info("Обновлен объект {}", user.getLogin());
    }

    public Collection<User> returnAllUsers() {
        return userStorage.returnAllUsers();
    }

    public void addUserFriend(Integer id, Integer friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        user.setUserFriend(userFriend);
        userFriend.setUserFriend(user);
    }

    public void removeUserFriend(Integer id, Integer friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        user.removeUserFriend(userFriend);
        userFriend.removeUserFriend(user);
    }

    public Collection<User> getUserFriends(Integer id) {
        return findUserById(id).getUserFriends();
    }

    public Collection<User> getCommonUserFriends(Integer id, Integer otherId) {
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        HashSet<User> commonFriends = new HashSet<>();
        for (User u : user.getUserFriends()) {
            for (User u2 : otherUser.getUserFriends()) {
                if (u.equals(u2)) {
                    commonFriends.add(u);
                }
            }
        }
        return commonFriends;
    }

    public User findUserById(Integer id) {
        return userStorage.returnAllUsers()
                .stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    private boolean validateDate(User user) {
        return user.getLogin().contains(" ") || !user.getEmail().contains("@");
    }

}
