package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.print.DocFlavor;
import java.util.Collection;
import java.util.Map;

@Repository
@Primary
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration, mpa)" +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_MPA = "INSERT INTO films(mpa)" +
            "VALUES (?);";
    private static final String INSERT_GENRE = "INSERT INTO film_genres(filmId, genreId)" +
            "VALUES (?, ?);";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa = ? WHERE id = ?;";
    private static final String FIND_FILM_BY_GENRE = "SELECT * FROM films RIGHT JOIN film_genres" +
            "ON films.id = film_genres.filmId" +
            "WHERE film_genres.genreId = ?;";


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
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
    public Film getFilmByGenre(long genreId) {
        return findOne(FIND_FILM_BY_GENRE, genreId);
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
