package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    Collection<Friend> getAll();
    Collection<Friend> getFriendsById(long id);
    void addFriend(long id, long friendId);
    void removeFriend(long id, long friendId);

}
