package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Comparable<Film> {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    MPA mpa;
    private Set<Genre> genres = new HashSet<>();
    private long likes;

    public Film(String name, String description, LocalDate releaseDate, long duration, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    @Override
    public int compareTo(Film film) {
        return (int) film.likes - (int) this.likes;
    }
}