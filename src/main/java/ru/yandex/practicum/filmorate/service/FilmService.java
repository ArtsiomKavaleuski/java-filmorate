package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.*;

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
        film = fillMpaGenresLikes(filmStorage.create(film));
        log.info("Фильм добавлен и ему присвоен id = {}", film.getId());
        return film;
    }

    public Film update(@Valid Film newFilm) {
        newFilm = fillMpaGenresLikes(filmStorage.update(newFilm));
        log.info("Фильм с id {} изменен", newFilm.getId());
        return newFilm;
    }

    public void addLike(long filmId, long userId) {
        filmId = getFilmById(filmId).getId();
        userId = userStorage.getUserById(userId).getId();
        likeStorage.addLike(filmId, userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        filmId = getFilmById(filmId).getId();
        userId = userStorage.getUserById(userId).getId();
        likeStorage.removeLike(filmId, userId);
        log.info("Пользователь с id = {} удалил свой лайк фильму с id = {}.", userId, filmId);
    }

    public Collection<Film> getPopularFilms(@Positive int count) {
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
}