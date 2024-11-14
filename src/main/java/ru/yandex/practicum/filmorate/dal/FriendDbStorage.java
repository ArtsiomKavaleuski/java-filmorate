package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collection;

@Repository
public class FriendDbStorage extends BaseDbStorage<Friend> implements FriendStorage {
    private static final String FIND_FRIENDS_BY_ID_QUERY = "SELECT * FROM filmorate.friends WHERE userId = ?;";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM filmorate.friends " +
            "WHERE userId = ? AND friendId = ?;";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO filmorate.friends(userId, friendId) VALUES (?, ?);";
    private static final String UPDATE_RECIPROCITY_QUERY = "UPDATE filmorate.friends SET reciprocity = ? " +
            "WHERE userId = ? AND friendId = ?;";

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Friend> getFriendsById(long id) {
        return findMany(FIND_FRIENDS_BY_ID_QUERY, id);
    }

    @Override
    public void addFriend(long id, long friendId) {
        update(INSERT_FRIEND_QUERY, id, friendId);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        delete(DELETE_FRIEND_QUERY, id, friendId);
    }

    @Override
    public void updateReciprocity(long id, long friendId, boolean reciprocity) {
        update(UPDATE_RECIPROCITY_QUERY, reciprocity, id, friendId);
    }
}