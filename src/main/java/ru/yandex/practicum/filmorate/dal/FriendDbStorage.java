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
    private final String FIND_ALL_MPA_QUERY = "SELECT * FROM friends;";
    private final String FIND_MPA_BY_ID_QUERY = "SELECT * FROM friends WHERE userId = ?;";

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
        return findMany(FIND_MPA_BY_ID_QUERY, id);
    }
}
