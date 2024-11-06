package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendRowMapper implements RowMapper<Friend> {
    @Override
    public Friend mapRow(ResultSet rs, int numRom) throws SQLException {
        Friend friend = new Friend();
        friend.setUserId(rs.getInt("userId"));
        friend.setFriendId(rs.getInt("friendId"));
        friend.setFriendship(rs.getBoolean("friendship"));
        return friend;
    }
}
