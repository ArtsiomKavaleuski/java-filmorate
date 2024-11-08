package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage extends BaseDbStorage<Like> implements LikeStorage {
    private static final String INSERT_LIKE_QUERY = "text";
    private static final String DELETE_LIKE_QUERY = "text";

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Like addLike(long filmId, long userId) {
        insert(
                INSERT_LIKE_QUERY,
                filmId,
                userId
        );
        Like like = new Like();
        like.setFilmId(filmId);
        like.setUserId(userId);
        return like;
    }

    @Override
    public Like removeLike(long filmId, long userId) {
        delete(
                DELETE_LIKE_QUERY,
                filmId,
                userId
        );
        Like like = new Like();
        like.setFilmId(filmId);
        like.setUserId(userId);
        return like;
    }
}
