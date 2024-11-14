package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User getUserById(long id);

    User create(User user);

    User update(User newUser);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long id, long friendId);
}