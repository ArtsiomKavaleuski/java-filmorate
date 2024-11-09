package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {

    private final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres;";
    private final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE genreId = ?;";
    private final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(filmId, genreId) VALUES(?, ?);";
    private final String FIND_FILM_GENRES_QUERY = "SELECT genres.genreId, genres.genreName FROM genres " +
            "RIGHT JOIN film_genres ON genres.genreId = film_genres.genreId " +
            "WHERE film_genres.filmId = ?;";

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAll() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    @Override
    public Genre getGenreById(long id) {
        return findOne(FIND_GENRE_BY_ID_QUERY, id);
    }

    @Override
    public void addFilmGenres(long filmId, long genreId) {
        update(INSERT_FILM_GENRES_QUERY, filmId, genreId);
    }

    @Override
    public Collection<Genre> getGenresByFilm(long filmId) {
        return findMany(FIND_FILM_GENRES_QUERY, filmId);
    }
}