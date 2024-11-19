package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    @Autowired
    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(long id) {
        if (genreStorage.getGenreById(id) == null) {
            log.warn("Жанр с id = {} не найден.", id);
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
        return genreStorage.getGenreById(id);
    }
}