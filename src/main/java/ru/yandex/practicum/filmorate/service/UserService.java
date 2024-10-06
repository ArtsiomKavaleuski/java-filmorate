package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userStorage.getUserById(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User addFriend(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        if (userStorage.getUserById(friendId) == null) {
            log.warn("Пользователь, добавляемый в друзья с id = {} не найден", friendId);
            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + friendId + " не найден.");
        }
        if (id == friendId) {
            log.warn("Пользователя с id {} нельзя добавить в друзья к самому себе.", id);
            throw new ValidationException("Пользователя с id " + id + " нельзя добавить в друзья к самому себе.");
        }
        userStorage.getUserById(id).addToFriends(friendId);
        userStorage.getUserById(friendId).addToFriends(id);
        log.info("Пользователю с id = {} в друзья добавлен пользователь с id = {}.", id, friendId);
        return userStorage.getUserById(friendId);
    }

    public User removeFriend(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        if (userStorage.getUserById(friendId) == null) {
            log.warn("Пользователь с id = {}, удаляемый из друзей, не найден", friendId);
            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + friendId + " не найден.");
        }
        userStorage.getUserById(id).removeFromFriends(friendId);
        userStorage.getUserById(friendId).removeFromFriends(id);
        return userStorage.getUserById(friendId);
    }

    public Collection<User> getAllFriends(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        return userStorage.getUserById(id).getFriends().stream().map(userStorage::getUserById).toList();
    }

    public List<User> getCommonFriends(long id, long otherId) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        if (userStorage.getUserById(otherId) == null) {
            log.warn("Пользователь, добавляемый в друзья с id = {} не найден", otherId);
            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + otherId + " не найден.");
        }
        return userStorage.getUserById(id).getFriends().stream()
                .filter(l -> userStorage.getUserById(otherId).getFriends().contains(l))
                .map(userStorage::getUserById)
                .toList();
    }


}
