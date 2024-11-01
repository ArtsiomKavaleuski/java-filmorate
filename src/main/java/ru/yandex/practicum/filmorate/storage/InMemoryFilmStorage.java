//package ru.yandex.practicum.filmorate.storage;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class InMemoryFilmStorage implements FilmStorage {
//    private final Map<Long, Film> films = new HashMap<>();
//
//    public Collection<Film> getAll() {
//        return films.values();
//    }
//
//    public Film getFilmById(long id) {
//        return films.getOrDefault(id, null);
//    }
//
//    public Film create(Film film) {
//        validateFilm(film);
//        film.setId(getNextId());
//        films.put(film.getId(), film);
//        log.info("Фильм добавлен и ему присвоен id = {}", film.getId());
//        return film;
//    }
//
//    public Film update(Film newFilm) {
//        validateFilm(newFilm);
//        if (films.containsKey(newFilm.getId())) {
//            films.put(newFilm.getId(), newFilm);
//            log.info("Фильм с id {} изменен", newFilm.getId());
//            return newFilm;
//        }
//        log.warn("Фильм с id = {} не найден", newFilm.getId());
//        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
//    }
//
//    private long getNextId() {
//        long currentMaxId = films.keySet()
//                .stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        return ++currentMaxId;
//    }
//
//
//}
