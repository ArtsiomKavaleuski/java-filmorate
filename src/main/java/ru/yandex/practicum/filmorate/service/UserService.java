package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class UserService {
    @Qualifier("UserDbStorage")
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
        return userStorage.update(user);
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
        if (userStorage.getFriends(id).contains(userStorage.getUserById(friendId))) {
            log.warn("Пользоватль с id {} уже добавлен в друзья к указанному пользователю", friendId);
            throw new DuplicateRequestException("Пользоватль с id " + friendId + " уже добавлен в друзья к указанному пользователю");

        }
        userStorage.addFriend(id, friendId);
        log.info("Пользователю с id = {} в друзья добавлен пользователь с id = {}.", id, friendId);
        return userStorage.getUserById(friendId);
    }
//
//    public User removeFriend(long id, long friendId) {
//        if (userStorage.getUserById(id) == null) {
//            log.warn("Пользователь с id = {} не найден", id);
//            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
//        }
//        if (userStorage.getUserById(friendId) == null) {
//            log.warn("Пользователь с id = {}, удаляемый из друзей, не найден", friendId);
//            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + friendId + " не найден.");
//        }
//        userStorage.getUserById(id).removeFromFriends(friendId);
//        userStorage.getUserById(friendId).removeFromFriends(id);
//        return userStorage.getUserById(friendId);
//    }
//
    public Collection<User> getAllFriends(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
        return userStorage.getFriends(id);
    }
//
//    public List<User> getCommonFriends(long id, long otherId) {
//        if (userStorage.getUserById(id) == null) {
//            log.warn("Пользователь с id = {} не найден", id);
//            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
//        }
//        if (userStorage.getUserById(otherId) == null) {
//            log.warn("Пользователь, добавляемый в друзья с id = {} не найден", otherId);
//            throw new NotFoundException("Пользователь, добавляемый в друзья с id=" + otherId + " не найден.");
//        }
//        return userStorage.getUserById(id).getFriends().stream()
//                .filter(l -> userStorage.getUserById(otherId).getFriends().contains(l))
//                .map(userStorage::getUserById)
//                .toList();
//    }


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
