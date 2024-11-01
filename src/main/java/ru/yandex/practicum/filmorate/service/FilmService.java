package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
//    @Qualifier("UserDbStorage")
//    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        //this.userStorage = userStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(long id) {
        if (filmStorage.getFilmById(id) == null) {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return filmStorage.getFilmById(id);
    }

    public Film create(Film film) {
        validateFilm(film);
        log.info("Фильм добавлен и ему присвоен id = {}", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        validateFilm(newFilm);
        if (filmStorage.getFilmById(newFilm.getId()) == null) {
            log.warn("Фильм с id = {} не найден.", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.info("Фильм с id {} изменен", newFilm.getId());
        return filmStorage.update(newFilm);
    }

//    public void addLikeToFilm(long filmId, long userId) {
//        if (filmStorage.getFilmById(filmId) == null) {
//            log.warn("Фильм с id = {} не найден.", filmId);
//            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
//        }
//        if (userStorage.getUserById(userId) == null) {
//            log.warn("Пользователь с id = {} не может поставить лайк, так как не существует.", userId);
//            throw new NotFoundException("Пользователь с id = " + userId + " не может поставить лайк, так как не существует.");
//        }
//        filmStorage.getFilmById(filmId).addToLikes(userId);
//        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, filmId);
//    }
//
//    public void removeLikeFromFilm(long filmId, long userId) {
//        if (filmStorage.getFilmById(filmId) == null) {
//            log.warn("Фильм с id = {} не найден.", filmId);
//            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
//        }
//        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
//            log.warn("Лайк пользователя c id = {} фильму с id = {} не может быть удален, так как не был поставлен",
//                    userId, filmId);
//            throw new NotFoundException("Лайк пользователя с id = " + userId + " не может быть удален, так как не был им поставлен.");
//        }
//        filmStorage.getFilmById(filmId).removeFromLikes(userId);
//        log.info("Пользователь с id = {} удалил свой лайк фильму с id = {}.", userId, filmId);
//
//    }
//
//    public List<Film> getSortedLikedFilms(int count) {
//        if (count <= 0) {
//            log.warn("Размер выборки равен {}. Значение должно быть больше нуля.", count);
//            throw new ValidationException("Некорректный размер выборки. Размер должен быть больше нуля");
//        }
//        if (count > filmStorage.getAll().size()) {
//            count = filmStorage.getAll().size();
//        }
//        return filmStorage.getAll().stream().sorted().toList().subList(0, count);
//    }
private void validateFilm(Film film) {
    if (film.getName() == null || film.getName().isBlank()) {
        log.warn("Введенное название фильма пустое", film.getId());
        throw new ValidationException("Название не может быть пустым");
    }
    if (film.getDescription().length() > 200) {
        log.warn("Введенное описание фильма содержит {} из 200 возможных символов", film.getDescription().length());
        throw new ValidationException("Максимальная длина описания - 200 символов");
    }
    if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
        log.warn("Введенная дата релиза {} фильма не может быть раньше 28 декабря 1895 года", film.getReleaseDate());
        throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
    }
    if (film.getDuration() < 0) {
        log.warn("Введенная продолжительность {} фильма не является положительным числом", film.getDuration());
        throw new ValidationException("продолжительность фильма должна быть положительным числом");
    }
}
}
