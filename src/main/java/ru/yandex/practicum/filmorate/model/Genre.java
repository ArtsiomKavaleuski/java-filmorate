package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    private long id;
    private String name;

    public Genre(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Genre genre) {
        return (int) this.getId() - (int) genre.getId();
    }
}