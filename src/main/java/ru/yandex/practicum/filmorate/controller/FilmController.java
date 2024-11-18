package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Передан объект фильма {} для добавления.", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Передан модифицированный объект фильма {} для обновления.", newFilm);
        return filmService.update(newFilm);
    }

    @PutMapping("/{filmId}/like/{id}")
    public void addLikeToFilm(@PathVariable("filmId") long filmId, @PathVariable("id") long id) {
        log.info("Передан id = {} пользователя для добавления лайка фильму с id = {}", id, filmId);
        filmService.addLike(filmId, id);
    }

    @DeleteMapping("/{filmId}/like/{id}")
    public void removeLikeFromFilm(@PathVariable("filmId") long filmId, @PathVariable("id") long id) {
        log.info("Передан id = {} пользователя для удаления его лайка фильму с id = {}", id, filmId);
        filmService.removeLike(filmId, id);
    }
}