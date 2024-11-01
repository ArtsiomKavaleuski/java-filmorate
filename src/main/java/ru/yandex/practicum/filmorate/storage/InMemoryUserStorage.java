//package ru.yandex.practicum.filmorate.storage;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class InMemoryUserStorage implements UserStorage {
//    private final Map<Long, User> users = new HashMap<>();
//
//    public Collection<User> getAll() {
//        return users.values();
//    }
//
//    public User getUserById(long id) {
//        return users.getOrDefault(id, null);
//    }
//
//    public User create(User user) {
//        validateUser(user);
//        user.setId(getNextId());
//        users.put(user.getId(), user);
//        log.info("Пользователь добавлен и ему присвоен id = {}", user.getId());
//        return user;
//    }
//
//    public User update(User newUser) {
//        validateUser(newUser);
//        if (users.containsKey(newUser.getId())) {
//            users.put(newUser.getId(), newUser);
//            log.info("Пользователь с id = {} изменен", newUser.getId());
//            return newUser;
//        }
//        log.warn("Пользователь с id = {} не найден", newUser.getId());
//        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
//    }
//
//    private long getNextId() {
//        long currentMaxId = users.keySet()
//                .stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        return ++currentMaxId;
//    }
//
//    private void validateUser(User user) {
//        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
//            log.warn("Не указана электронная почта пользователя");
//            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
//        }
//        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
//            log.warn("Логин пользователя не указан или содержит пробелы");
//            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
//        }
//        if (user.getBirthday().isAfter(LocalDate.now())) {
//            log.warn("Введенная дата рождения пользователя некорректна");
//            throw new ValidationException("Дата рождения не может быть в будущем");
//        }
//        if (user.getName() == null || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//            log.info("Вместо имени пользователя использован логин");
//        }
//    }
//}
