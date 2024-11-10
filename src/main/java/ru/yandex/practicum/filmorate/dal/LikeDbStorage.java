package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;

@Repository
public class LikeDbStorage extends BaseDbStorage<Like> implements LikeStorage {
    private static final String INSERT_LIKE_QUERY = "INSERT INTO filmorate.likes(filmId, userId) VALUES(?, ?);";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM filmorate.likes WHERE filmId = ? AND userId = ?;";
    private static final String FIND_LIKES_BY_FILM_QUERY = "SELECT * FROM filmorate.likes WHERE filmId = ?;";
    private static final String FIND_LIKES_BY_FILMS_QUERY = "SELECT f.id, f.name, f.description, f.releaseDate, " +
            "f.duration, f.mpa, count(l.userId) AS likes FROM FILMORATE.FILMS f " +
            "LEFT JOIN FILMORATE.LIKES l ON f.ID = l.FILMID GROUP BY f.id ORDER BY likes DESC LIMIT ?;";

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(long filmId, long userId) {
        update(
                INSERT_LIKE_QUERY,
                filmId,
                userId
        );
    }

    @Override
    public void removeLike(long filmId, long userId) {
        delete(
                DELETE_LIKE_QUERY,
                filmId,
                userId
        );
    }

    @Override
    public Collection<Like> getLikesByFilm(long filmId) {
        return findMany(FIND_LIKES_BY_FILM_QUERY, filmId);
    }
}