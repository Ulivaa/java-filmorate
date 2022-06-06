package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.UserNotFoundException;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FriendshipStorage;
import ru.yandex.practicum.javafilmorate.storage.ReadUserStorage;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;
    private ReadUserStorage readUserStorage;
    private FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage, ReadUserStorage readUserStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.readUserStorage = readUserStorage;
    }

    public User addUser(User user) {
        if (validateDate(user) || readUserStorage.findUserByEmail(user.getEmail()).isPresent()) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }

        userStorage.save(user);
        log.info("Добавлен объект {}", user.getLogin());
        return findUserById(user.getId());
    }

    public User updateUser(User user) {
        findUserById(user.getId());
        if (validateDate(user)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        userStorage.update(user);
        log.info("Обновлен объект {}", user.getLogin());
        return findUserById(user.getId());

    }

    public Collection<User> returnAllUsers() {
        return userStorage.returnAllUsers();
    }

    public void addUserFriend(Integer id, Integer friendId) {
        if (findUserById(id) != null && findUserById(friendId) != null) {
            friendshipStorage.save(id, friendId);
        }
    }

    public void removeUserFriend(Integer id, Integer friendId) {
        if (findUserById(id) != null && findUserById(friendId) != null) {
            friendshipStorage.delete(id, friendId);
        }
    }

    public Collection<User> getUserFriends(Integer id) {
        return friendshipStorage.getFriends(id);
    }

    public Collection<User> getCommonUserFriends(Integer id, Integer otherId) {
        return friendshipStorage.getCommonFriends(id, otherId);
    }

    public User findUserById(Integer id) {
        return readUserStorage.findUserById(id).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    private boolean validateDate(User user) {
        return user.getLogin().contains(" ") || !user.getEmail().contains("@") || user.getId() < 0;
    }

}
