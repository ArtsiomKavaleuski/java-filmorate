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
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    long duration;
    Set<Long> likes = new HashSet<>();

    public void addToLikes(long id) {
        likes.add(id);
    }

    public void removeFromLikes(long id) {
        likes.remove(id);
    }

    @Override
    public int compareTo(Film film) {
        return film.likes.size() - this.likes.size();
    }
}
