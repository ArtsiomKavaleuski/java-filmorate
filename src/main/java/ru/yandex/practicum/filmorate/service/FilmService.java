package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmService {
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final GenreStorage genreStorage;
    @Autowired
    private final MpaStorage mpaStorage;
    @Autowired
    private final LikeStorage likeStorage;
    @Autowired
    private final UserStorage userStorage;

    public Collection<Film> getAll() {
        return fillMpaGenresLikes(filmStorage.getAll());
    }

    public Film getFilmById(long id) {
        return fillMpaGenresLikes(filmStorage.getFilmById(id));
    }

    public Film create(@Valid Film film) {
        validateFilm(film);
        film = fillMpaGenresLikes(filmStorage.create(film));
        log.info("Фильм добавлен и ему присвоен id = {}", film.getId());
        return film;
    }

    public Film update(Film newFilm) {
        validateFilm(newFilm);
        log.info("Фильм с id {} изменен", newFilm.getId());
        return fillMpaGenresLikes(filmStorage.update(newFilm));
    }

    public void addLike(long filmId, long userId) {
        checkFilmNotFound(filmId);
        checkUserNotFound(userId);
        likeStorage.addLike(filmId, userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        checkFilmNotFound(filmId);
        checkUserNotFound(userId);
        likeStorage.removeLike(filmId, userId);
        log.info("Пользователь с id = {} удалил свой лайк фильму с id = {}.", userId, filmId);
    }

    public Collection<Film> getPopularFilms(@Positive int count) {
//        if (count <= 0) {
//            log.warn("Размер выборки равен {}. Значение должно быть больше нуля.", count);
//            throw new ValidationException("Некорректный размер выборки. Размер должен быть больше нуля");
//        }
        if (count > filmStorage.getAll().size()) {
            count = filmStorage.getAll().size();
        }
        return fillMpaGenresLikes(filmStorage.getAll(count));
    }

    private Film fillMpaGenresLikes(Film film) {
        if (film.getMpa() != null) {
            MPA mpa = mpaStorage.getMpaById(film.getMpa().getId());
            film.setMpa(mpa);
        }
        if (film.getGenres() != null) {
            Set<Genre> filmGenres = new TreeSet<>();
            for (Genre genreWithoutName : film.getGenres()) {
                Genre genre = genreStorage.getGenreById(genreWithoutName.getId());
                genreStorage.addFilmGenres(film.getId(), genre.getId());
                filmGenres.add(genre);
            }
            film.setGenres(filmGenres);
        }
        if (genreStorage.getGenresByFilm(film.getId()) != null) {
            Set<Genre> filmGenres = new TreeSet<>(genreStorage.getGenresByFilm(film.getId()));
            film.setGenres(filmGenres);
        }
        if (likeStorage.getLikesByFilm(film.getId()) != null) {
            film.setLikes(likeStorage.getLikesByFilm(film.getId()).size());
        }
        return film;
    }

    private Collection<Film> fillMpaGenresLikes(Collection<Film> films) {
        Collection<Film> updatedFilms = new ArrayList<>();
        for (Film film : films) {
            if (film.getMpa() != null) {
                MPA mpa = mpaStorage.getMpaById(film.getMpa().getId());
                film.setMpa(mpa);
            }
            if (genreStorage.getGenresByFilm(film.getId()) != null) {
                Set<Genre> filmGenres = new TreeSet<>(genreStorage.getGenresByFilm(film.getId()));
                film.setGenres(filmGenres);
            }
            if (likeStorage.getLikesByFilm(film.getId()) != null) {
                film.setLikes(likeStorage.getLikesByFilm(film.getId()).size());
            }
            updatedFilms.add(film);
        }
        return updatedFilms;
    }

    private void validateFilm(Film film) {
//        if (film.getName() == null || film.getName().isBlank()) {
//            log.warn("Введенное название фильма пустое");
//            throw new ValidationException("Название не может быть пустым");
//        }
//        if (film.getDescription().length() > 200) {
//            log.warn("Введенное описание фильма содержит {} из 200 возможных символов",
//                    film.getDescription().length());
//            throw new ValidationException("Максимальная длина описания - 200 символов");
//        }
//        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
//            log.warn("Введенная дата релиза {} фильма не может быть раньше 28 декабря 1895 года",
//                    film.getReleaseDate());
//            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
//        }
//        if (film.getDuration() < 0) {
//            log.warn("Введенная продолжительность {} фильма не является положительным числом",
//                    film.getDuration());
//            throw new ValidationException("продолжительность фильма должна быть положительным числом");
//        }
//        if (film.getMpa().getId() < 1 || film.getMpa().getId() > 5) {
//            log.warn("Индекс введенного MPA {} не существует",
//                    film.getMpa().getId());
//            throw new ValidationException("Индекс введенного MPA не существует");
//        }
        for (Genre genre : film.getGenres()) {
            if (genre.getId() < 1 || genre.getId() > 6) {
                log.warn("Индекс введенного жанра {} не существует", genre.getId());
                throw new ValidationException("Индекс введенного жанра не существует");
            }
        }
    }

    private void checkFilmNotFound(long id) {
        if (filmStorage.getFilmById(id) == null) {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private void checkUserNotFound(long id) {
        if (userStorage.getUserById(id) == null) {
            log.warn("Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }
}