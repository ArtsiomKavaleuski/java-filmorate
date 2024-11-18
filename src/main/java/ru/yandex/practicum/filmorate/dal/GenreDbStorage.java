package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM filmorate.genres;";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM filmorate.genres WHERE genreId = ?;";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO filmorate.filmGenres(filmId, genreId) " +
            "VALUES(?, ?);";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.genreId, g.genreName FROM filmorate.genres AS g " +
            "RIGHT JOIN filmorate.filmGenres AS fg ON g.genreId = fg.genreId WHERE fg.filmId = ?;";

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
        return findOne(FIND_GENRE_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден."));
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