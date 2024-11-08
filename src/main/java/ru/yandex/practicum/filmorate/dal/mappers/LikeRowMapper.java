package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int numRow) throws SQLException {
        Like like = new Like();
        like.setFilmId(rs.getInt("filmId"));
        like.setUserId(rs.getInt("userId"));
        return like;
    }
}
