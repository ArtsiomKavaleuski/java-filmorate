package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<Like> getLikesByFilm(long filmId);
}