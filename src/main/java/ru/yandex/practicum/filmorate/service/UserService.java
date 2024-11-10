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
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final FriendStorage friendStorage;

    public Collection<User> getAll() {
        return fillFriends(userStorage.getAll());
    }

    public User getUserById(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
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
        if(userStorage.getUserById(user.getId()) == null) {
            log.warn("Пользователь с id = {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        log.info("Пользователь с id = {} изменен", user.getId());
        User newUser = userStorage.update(user);
        user.setFriends(new HashSet<>(friendStorage.getFriendsById(newUser.getId())));

        return newUser;
    }

    public void addFriend(long id, long friendId) {
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
        for(User user : userStorage.getFriends(id)) {
            if(user.equals(userStorage.getUserById(friendId))) {
                log.warn("Пользоватль с id {} уже добавлен в друзья к указанному пользователю", friendId);
                throw new DuplicateException("Пользоватль с id " + friendId + " уже добавлен в друзья к указанному пользователю");
            }
        }
        friendStorage.addFriend(id, friendId);
        log.info("Пользователю с id = {} в друзья добавлен пользователь с id = {}.", id, friendId);
    }

    public User removeFriend(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        if (id == friendId) {
            log.warn("Пользователя с id {} нельзя удалить из друзей у самого себя.", id);
            throw new ValidationException("Пользователя с id " + id + " нельзя удалить из друзей у самого себя.");
        }
        if (userStorage.getUserById(friendId) == null) {
            log.warn("Пользователь с id = {}, удаляемый из друзей, не найден", friendId);
            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + friendId + " не найден.");
        }
        friendStorage.removeFriend(id, friendId);
        return userStorage.getUserById(friendId);
    }

    public Collection<User> getAllFriends(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        //return fillFriends(userStorage.getFriends(id));
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Первый пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        if (userStorage.getUserById(friendId) == null) {
            log.warn("Второй пользователь, добавляемый в друзья с id = {} не найден", friendId);
            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + friendId + " не найден.");
        }
        if (id == friendId) {
            log.warn("Попытка получить общий список друзей одного и того же пользователя");
            throw new NotFoundException("Попытка получить общий список друзей одного и того же пользователя");
        }
        return fillFriends(userStorage.getCommonFriends(id, friendId));
    }

    private Collection<User> fillFriends(Collection<User> users) {
        for(User user : users) {
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
}
