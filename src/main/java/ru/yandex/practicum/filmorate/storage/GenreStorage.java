package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> getAll();

    Genre getGenreById(long id);

    void addFilmGenres(long filmId, long genreId);

    Collection<Genre> getGenresByFilm(long filmId);
}