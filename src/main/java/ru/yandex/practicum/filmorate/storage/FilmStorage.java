package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAll();

    Collection<Film> getAll(int count);

    Film getFilmById(long id);

    Film create(Film film);

    Film update(Film newFilm);
}