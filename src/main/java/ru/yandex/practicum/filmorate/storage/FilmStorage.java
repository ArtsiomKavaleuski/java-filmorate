package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAll();

    Film getFilmById(long id);

    Film getFilmByGenre(long genreId);

    Film create(Film film);

    Film update(Film newFilm);
}

