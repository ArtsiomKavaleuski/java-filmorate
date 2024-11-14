package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM filmorate.films;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM filmorate.films WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO filmorate.films(name, description, releaseDate, " +
            "duration, mpa) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE filmorate.films SET name = ?, description = ?, " +
            "releaseDate = ?, duration = ?, mpa = ? WHERE id = ?;";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.id, f.name, f.description, f.releaseDate, " +
            "f.duration, f.mpa, count(l.userId) AS likes FROM filmorate.films f " +
            "LEFT JOIN filmorate.likes l ON f.id = l.filmId GROUP BY f.id ORDER BY likes DESC LIMIT ?;";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<Film> getAll(int count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public Film getFilmById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Film create(Film newFilm) {
        long id = insert(
                INSERT_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId()
        );
        newFilm.setId(id);
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }
}