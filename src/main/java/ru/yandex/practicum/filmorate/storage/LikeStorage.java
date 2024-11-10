package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    Like addLike(long filmId, long userId);
    Like removeLike(long filmId, long userId);
    Collection<Like> getLikesByFilm(long filmId);
}
