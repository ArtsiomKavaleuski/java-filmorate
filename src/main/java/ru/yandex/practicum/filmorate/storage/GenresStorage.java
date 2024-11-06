package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenresStorage {
    Collection<Genre> getAll();

    Genre getGenreById(long id);
}
