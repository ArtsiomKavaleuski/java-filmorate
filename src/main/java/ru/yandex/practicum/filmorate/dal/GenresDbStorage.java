package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;

@Repository
public class GenresDbStorage extends BaseDbStorage<Genre> implements GenresStorage {

    private final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres;";
    private final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE genreId = ?;";

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
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
}