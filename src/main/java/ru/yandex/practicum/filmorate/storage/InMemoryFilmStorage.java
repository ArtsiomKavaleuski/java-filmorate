package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film getFilmById(long id) {return films.getOrDefault(id, null);}

    public Film create(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм с id = {}", film.getId());
        return film;
    }

    public Film update(Film newFilm) {
        validateFilm(newFilm);
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.info("Изменен фильм с id {}", newFilm.getId());
            return newFilm;
        }
        log.warn("Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Введенное название фильма с id = {} пустое", film.getId());
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Введенное описание фильма с id = {} содержит {} из 200 возможных символов", film.getId(), film.getDescription().length());
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Введенная дата релиза {} фильма с id = {} не может быть раньше 28 декабря 1895 года", film.getReleaseDate(), film.getId());
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Введенная продолжительность {} фильма с id = {} не является положительным числом", film.getDuration(), film.getId());
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }
    }
}
