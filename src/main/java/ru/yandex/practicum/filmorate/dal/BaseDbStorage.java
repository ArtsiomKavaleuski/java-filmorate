package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InsertFailedException;
import ru.yandex.practicum.filmorate.exception.UpdateFailedException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            Optional<T> result = Optional.ofNullable(jdbc.queryForObject(query, mapper, params));
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected Collection<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new UpdateFailedException("При попытке выполнить SQL запрос [" + query + "] " +
                    "с параметрами запроса [" + Arrays.toString(params) + "] произошла ошибка.");
        }
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        if (keyHolder.getKey() != null) {
            return id;
        } else {
            throw new InsertFailedException("При попытке выполнить SQL запрос [" + query + "] " +
                    "с параметрами запроса [" + Arrays.toString(params) + "] произошла ошибка.");
        }
    }
}