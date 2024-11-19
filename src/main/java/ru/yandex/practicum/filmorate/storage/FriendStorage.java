package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Collection;

public interface FriendStorage {
    Collection<Friend> getFriendsById(long id);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    void updateReciprocity(long id, long friendId, boolean reciprocity);
}