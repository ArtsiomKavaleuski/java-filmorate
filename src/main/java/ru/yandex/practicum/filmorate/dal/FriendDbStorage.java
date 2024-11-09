package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collection;

@Repository
public class FriendDbStorage extends BaseDbStorage<Friend> implements FriendStorage {
    private static final String FIND_ALL_MPA_QUERY = "SELECT * FROM friends;";
    private static final String FIND_FRIEND_BY_ID_QUERY = "SELECT * FROM friends WHERE userId = ?;";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE userId = ? AND friendId = ?;";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends(userId, friendId) VALUES (?, ?);";

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }
    @Override
    public Collection<Friend> getAll() {
        return findMany(FIND_ALL_MPA_QUERY);
    }
    @Override
    public Collection<Friend> getFriendsById(long id) {
        return findMany(FIND_FRIEND_BY_ID_QUERY, id);
    }
    @Override
    public void addFriend(long id, long friendId) {
        update(INSERT_FRIEND_QUERY, id, friendId);
    }
    @Override
    public void removeFriend(long id, long friendId) {
        delete(DELETE_FRIEND_QUERY, id, friendId);
    }
}
