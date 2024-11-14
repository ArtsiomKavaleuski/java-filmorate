package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final FriendStorage friendStorage;

    public Collection<User> getAll() {
        return fillFriends(userStorage.getAll());
    }

    public User getUserById(long id) {
        checkUserNotFound(id);
        User user = userStorage.getUserById(id);
        user.setFriends(new HashSet<>(friendStorage.getFriendsById(id)));
        return user;
    }

    public User create(User user) {
        validateUser(user);
        user = userStorage.create(user);
        log.info("Пользователь добавлен и ему присвоен id = {}", user.getId());
        return user;
    }

    public User update(User user) {
        validateUser(user);
        checkUserNotFound(user.getId());
        User newUser = userStorage.update(user);
        log.info("Пользователь с id = {} изменен", newUser.getId());
        newUser.setFriends(new HashSet<>(friendStorage.getFriendsById(newUser.getId())));
        return newUser;
    }

    public void addFriend(long id, long friendId) {
        checkUserNotFound(id);
        checkUserNotFound(friendId);
        checkIds(id, friendId);
        for (User user : userStorage.getFriends(id)) {
            if (user.equals(userStorage.getUserById(friendId))) {
                log.warn("Пользоватль с id {} уже добавлен в друзья к указанному пользователю", friendId);
                throw new DuplicateException("Пользоватль с id " + friendId + " уже добавлен в друзья");
            }
        }
        friendStorage.addFriend(id, friendId);
        if (!friendStorage.getFriendsById(friendId).isEmpty()
                && friendStorage.getFriendsById(friendId).stream().anyMatch(f -> f.getFriendId() == id)) {
            friendStorage.updateReciprocity(id, friendId, Boolean.TRUE);
            friendStorage.updateReciprocity(friendId, id, Boolean.TRUE);
        }
        log.info("Пользователю с id = {} в друзья добавлен пользователь с id = {}.", id, friendId);
    }

    public void removeFriend(long id, long friendId) {
        checkUserNotFound(id);
        checkUserNotFound(friendId);
        checkIds(id, friendId);
        friendStorage.removeFriend(id, friendId);
        if (friendStorage.getFriendsById(friendId).stream().anyMatch(f -> f.getFriendId() == id)) {
            friendStorage.updateReciprocity(friendId, id, Boolean.FALSE);
        }
        log.info("Из друзей пользователя с id = {} удален пользователь с id = {}.", id, friendId);
    }

    public Collection<User> getAllFriends(long id) {
        checkUserNotFound(id);
        return fillFriends(userStorage.getFriends(id));
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        checkUserNotFound(id);
        checkUserNotFound(friendId);
        checkIds(id, friendId);
        return fillFriends(userStorage.getCommonFriends(id, friendId));
    }

    private Collection<User> fillFriends(Collection<User> users) {
        for (User user : users) {
            user.setFriends(new HashSet<>(friendStorage.getFriendsById(user.getId())));
        }
        return users;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Не указана электронная почта пользователя");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин пользователя не указан или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Введенная дата рождения пользователя некорректна");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Вместо имени пользователя использован логин");
        }
    }

    private void checkUserNotFound(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private void checkIds(long id, long friendId) {
        if (id == friendId) {
            log.warn("Введен один и тот же id = {}.", id);
            throw new NotFoundException("Введен один и тот же id.");
        }
    }
}