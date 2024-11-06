package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenresService {
    private final GenresStorage genresStorage;

    @Autowired
    public GenresService(GenresStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    public Collection<Genre> getAll() {
        return genresStorage.getAll();
    }

    public Genre getGenreById(long id) {
        if (genresStorage.getGenreById(id) == null) {
            log.warn("Жанр с id = {} не найден.", id);
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
        return genresStorage.getGenreById(id);
    }
}
