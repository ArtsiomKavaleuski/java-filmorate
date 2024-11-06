package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenresService genresService;

    @Autowired
    public GenreController(GenresService genresService) {
        this.genresService = genresService;
    }

    @GetMapping
    public Collection<Genre> getAll() {
        return genresService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") long id) {
        return genresService.getGenreById(id);
    }
}
