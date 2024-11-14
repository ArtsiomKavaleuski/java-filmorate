package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MpaStorage {
    Collection<MPA> getAll();

    MPA getMpaById(long id);

    MPA getMpaByFilm(long filmId);
}