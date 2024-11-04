package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        return filmService.getFilmById(id);
    }

//    @GetMapping("/popular")
//    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
//        return filmService.getSortedLikedFilms(count);
//    }

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

//    @PutMapping("/{id}/like/{userId}")
//    public void addLikeToFilm(@PathVariable("id") long id, @PathVariable("userId") long userId) {
//        log.info("Передан id = {} пользователя для добавления лайка фильму с id = {}", userId, id);
//        filmService.addLikeToFilm(id, userId);
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public void removeLikeFromFilm(@PathVariable("id") long id, @PathVariable("userId") long userId) {
//        log.info("Передан id = {} пользователя для удаления его лайка фильму с id = {}", userId, id);
//        filmService.removeLikeFromFilm(id, userId);
//    }


}