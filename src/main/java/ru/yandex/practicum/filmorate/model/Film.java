package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film implements Comparable<Film> {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    MPA mpa = new MPA();
    private Set<Genre> genres = new HashSet<>();
    private Set<Like> likes = new HashSet<>();

    @Override
    public int compareTo(Film film) {
        return film.likes.size() - this.likes.size();
    }
}
