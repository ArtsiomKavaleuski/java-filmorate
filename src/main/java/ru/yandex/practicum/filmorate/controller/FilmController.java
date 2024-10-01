package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmStorage.update(newFilm);
    }
}