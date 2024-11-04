package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Repository
@Primary
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration)" +
            "VALUES (?, ?, ?, ?);";
    private static final String INSERT_MPA = "INSERT INTO films(mpa)" +
            "VALUES (?);";
    private static final String INSERT_GENRE = "INSERT INTO genres(filmId, genreId)" +
            "VALUES (?, ?);";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE id = ?;";


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Collection<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
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
                newFilm.getDuration()
        );
        newFilm.setId(id);
        if (newFilm.getMpa() != null) {
            insert(INSERT_MPA, newFilm.getMpa().getId());
        }
        if (!newFilm.getGenres().isEmpty()) {
            for (Genre genre : newFilm.getGenres()) {
                insert(INSERT_GENRE, newFilm.getId(), genre.getId());
            }
        }
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
                newFilm.getId()
        );
        return newFilm;
    }
}
