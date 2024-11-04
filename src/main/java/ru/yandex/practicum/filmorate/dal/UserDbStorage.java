package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends(userId, friendId)" +
            "VALUES (?, ?);";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN (SELECT friendId FROM friends WHERE userId = ?);";

    //private final FriendsStorage friendsStorage;

    //public UserDbStorage(FriendsStorage friendsStorage) {
    //    this.friendsStorage = friendsStorage;
    //}

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getUserById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
                );
        return newUser;
    }

    public long addFriend(long id, long friendId) {
        insert(
                INSERT_FRIEND_QUERY,
                id,
                friendId
        );
//        insert(
//                INSERT_FRIEND_QUERY,
//                friendId,
//                id
//        );
        return friendId;
    }

    public Collection<User> getFriends(long id) {
        return findMany(FIND_FRIENDS_QUERY, id);
    }


}
