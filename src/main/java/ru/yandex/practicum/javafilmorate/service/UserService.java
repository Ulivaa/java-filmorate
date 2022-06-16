package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.javafilmorate.exception.UserNotFoundException;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FriendshipStorage;
import ru.yandex.practicum.javafilmorate.storage.ReadUserStorage;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        // проверка валидации и уже существующего пользователя с таким логином
        if (!validateDate(user) || readUserStorage.findUserByEmail(user.getEmail()).isPresent()) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }

        int id = userStorage.save(user);
        log.info("Добавлен объект {}", user.getLogin());
        return findUserById(id);
    }

    public void deleteUser(Integer userId) {
        if (findUserById(userId) != null) {
            userStorage.delete(userId);
        }
    }


    public User updateUser(User user) {
        findUserById(user.getId());
        if (!validateDate(user)) {
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
        findUserById(id);
        return friendshipStorage.getFriends(id);
    }

    public Collection<User> getCommonUserFriends(Integer id, Integer otherId) {
        return friendshipStorage.getCommonFriends(id, otherId);
    }

    public User findUserById(Integer id) {
        return readUserStorage.findUserById(id).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    public Collection<Event> findEventsFriendsUser(int id) {
        Collection<Event> eventsUser = readUserStorage.findEventsUser(id);
        return eventsUser.stream().
                sorted(this::sortedEventByDate).collect(Collectors.toList());
    }

    public int sortedEventByDate(Event o1, Event o2) {
        if (o1.getTimestamp().isAfter(o2.getTimestamp()))
            return -1;
        else if (o1.getTimestamp().isBefore(o2.getTimestamp()))
            return 1;
        else
            return 0;
    }

    private boolean validateDate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неверный формат данных");
            throw new IncorrectParameterException("birthday");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Неверный формат данных");
            throw new IncorrectParameterException("login");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Неверный формат данных");
            throw new IncorrectParameterException("email");
        }
        return true;
    }

}
