package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserService {
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final FriendStorage friendStorage;

    public Collection<User> getAll() {
        return fillFriends(userStorage.getAll());
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        user.setFriends(new HashSet<>(friendStorage.getFriendsById(id)));
        return user;
    }

    public User create(@Valid User user) {
        checkUserName(user);
        user = userStorage.create(user);
        log.info("Пользователь добавлен и ему присвоен id = {}", user.getId());
        return user;
    }

    public User update(@Valid User newUser) {
        checkUserName(newUser);
        getUserById(newUser.getId());
        newUser = userStorage.update(newUser);
        log.info("Пользователь с id = {} изменен", newUser.getId());
        newUser.setFriends(new HashSet<>(friendStorage.getFriendsById(newUser.getId())));
        return newUser;
    }

    public void addFriend(long id, long friendId) {
        long checkedUserId = getUserById(id).getId();
        long checkedFriendId = getUserById(friendId).getId();
        checkIds(checkedUserId, checkedFriendId);
        for (User user : userStorage.getFriends(checkedUserId)) {
            if (user.equals(userStorage.getUserById(checkedFriendId))) {
                log.warn("Пользоватль с id {} уже добавлен в друзья к указанному пользователю", checkedFriendId);
                throw new DuplicateException("Пользоватль с id " + checkedFriendId + " уже добавлен в друзья");
            }
        }
        friendStorage.addFriend(checkedUserId, checkedFriendId);
        if (!friendStorage.getFriendsById(checkedFriendId).isEmpty()
                && friendStorage.getFriendsById(checkedFriendId).stream()
                .anyMatch(f -> f.getFriendId() == checkedUserId)) {
            friendStorage.updateReciprocity(checkedUserId, checkedFriendId, Boolean.TRUE);
            friendStorage.updateReciprocity(checkedFriendId, checkedUserId, Boolean.TRUE);
        }
        log.info("Пользователю с id = {} в друзья добавлен пользователь с id = {}.", checkedUserId, checkedFriendId);
    }

    public void removeFriend(long id, long friendId) {
        long checkedUserId = getUserById(id).getId();
        long checkedFriendId = getUserById(friendId).getId();
        checkIds(checkedUserId, checkedFriendId);
        friendStorage.removeFriend(checkedUserId, checkedFriendId);
        if (friendStorage.getFriendsById(checkedFriendId).stream()
                        .anyMatch(f -> f.getFriendId() == checkedUserId)) {
            friendStorage.updateReciprocity(checkedFriendId, checkedUserId, Boolean.FALSE);
        }
        log.info("Из друзей пользователя с id = {} удален пользователь с id = {}.", checkedUserId, checkedFriendId);
    }

    public Collection<User> getAllFriends(long id) {
        id = getUserById(id).getId();
        return fillFriends(userStorage.getFriends(id));
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        id = getUserById(id).getId();
        friendId = getUserById(friendId).getId();
        checkIds(id, friendId);
        return fillFriends(userStorage.getCommonFriends(id, friendId));
    }

    private Collection<User> fillFriends(Collection<User> users) {
        for (User user : users) {
            user.setFriends(new HashSet<>(friendStorage.getFriendsById(user.getId())));
        }
        return users;
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Вместо имени пользователя использован логин");
        }
    }

    private void checkIds(long id, long friendId) {
        if (id == friendId) {
            log.warn("Введен один и тот же id = {}.", id);
            throw new NotFoundException("Введен один и тот же id.");
        }
    }
}