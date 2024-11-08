package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeStorage {
    Like addLike(long filmId, long userId);
    Like removeLike(long filmId, long userId);
}
