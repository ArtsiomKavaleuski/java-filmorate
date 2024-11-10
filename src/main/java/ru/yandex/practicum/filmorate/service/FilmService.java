package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
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
        return fillMpaAndGenres(filmStorage.getAll());
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.warn("Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        film = fillMpaAndGenres(filmStorage.getFilmById(id));
        return film;
    }

    public Film create(Film film) {
        validateFilm(film);
        film = fillMpaAndGenres(filmStorage.create(film));
        log.info("Фильм добавлен и ему присвоен id = {}", film.getId());
        return film;
    }

    public Film update(Film newFilm) {
        validateFilm(newFilm);
        if (filmStorage.getFilmById(newFilm.getId()) == null) {
            log.warn("Фильм с id = {} не найден.", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.info("Фильм с id {} изменен", newFilm.getId());
        newFilm = fillMpaAndGenres(filmStorage.update(newFilm));
        return newFilm;
    }

    public Like addLikeToFilm(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Фильм с id = {} не найден.", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            log.warn("Пользователь с id = {} не может поставить лайк, так как не существует.", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не может поставить лайк, так как не существует.");
        }
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, filmId);
        return likeStorage.addLike(filmId, userId);

    }

    public Like removeLikeFromFilm(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Фильм с id = {} не найден.", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
//        if (likeStorage.getLike(filmId, userId) == null) {
//            log.warn("Лайк пользователя c id = {} фильму с id = {} не может быть удален, так как не был поставлен",
//                    userId, filmId);
//            throw new NotFoundException("Лайк пользователя с id = " + userId + " не может быть удален, так как не был им поставлен.");
//        }
        log.info("Пользователь с id = {} удалил свой лайк фильму с id = {}.", userId, filmId);
        return likeStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getSortedLikedFilms(int count) {
        if (count <= 0) {
            log.warn("Размер выборки равен {}. Значение должно быть больше нуля.", count);
            throw new ValidationException("Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (count > filmStorage.getAll().size()) {
            count = filmStorage.getAll().size();
        }
        return fillMpaAndGenres(filmStorage.getAll(count));
    }

    private Film fillMpaAndGenres(Film film) {
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
            Set<Genre> filmGenres = new TreeSet<>();
            for (Genre genre : genreStorage.getGenresByFilm(film.getId())) {
                filmGenres.add(genre);
            }
            film.setGenres(filmGenres);
        }
        if(likeStorage.getLikesByFilm(film.getId()) != null) {
            film.setLikes(likeStorage.getLikesByFilm(film.getId()).size());
        }

        return film;
    }

    private Collection<Film> fillMpaAndGenres(Collection<Film> films) {
        Collection<Film> updatedFilms = new ArrayList<>();
        for (Film film : films) {
            if (film.getMpa() != null) {
                MPA mpa = mpaStorage.getMpaByFilm(film.getId());
                film.setMpa(mpa);
            }
            if (genreStorage.getGenresByFilm(film.getId()) != null) {
                Set<Genre> filmGenres = new TreeSet<>();
                for (Genre genre : genreStorage.getGenresByFilm(film.getId())) {
                    filmGenres.add(genre);
                }
                film.setGenres(filmGenres);
            }
            if(likeStorage.getLikesByFilm(film.getId()) != null) {
                film.setLikes(likeStorage.getLikesByFilm(film.getId()).size());
            }
            updatedFilms.add(film);
        }
        return updatedFilms;
    }

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

        if (film.getMpa().getId() < 1 || film.getMpa().getId() > 5) {
            log.warn("Индекс введенного MPA {} не существует", film.getMpa().getId());
            throw new ValidationException("Индекс введенного MPA не существует");
        }

        for (Genre genre : film.getGenres()) {
            if (genre.getId() < 1 || genre.getId() > 6) {
                log.warn("Индекс введенного жанра {} не существует", genre.getId());
                throw new ValidationException("Индекс введенного жанра не существует");
            }
        }
    }
}
