package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    @Min(value = 1, message = "Индекс жанра должен быть от 1 до 6")
    @Max(value = 6, message = "Индекс жанра должен быть от 1 до 6")
    private long id;
    private String name;

    @Override
    public int compareTo(Genre genre) {
        return (int) this.getId() - (int) genre.getId();
    }
}