package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Repository
public class MPADbStorage extends BaseDbStorage<MPA> implements MpaStorage {
    private static final String FIND_ALL_MPA_QUERY = "SELECT * FROM filmorate.mpa;";
    private static final String FIND_MPA_BY_ID_QUERY = "SELECT * FROM filmorate.mpa WHERE id = ?;";

    @Autowired
    public MPADbStorage(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<MPA> getAll() {
        return findMany(FIND_ALL_MPA_QUERY);
    }

    @Override
    public MPA getMpaById(long id) {
        return findOne(FIND_MPA_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с id = " + id + " не найден."));
    }
}