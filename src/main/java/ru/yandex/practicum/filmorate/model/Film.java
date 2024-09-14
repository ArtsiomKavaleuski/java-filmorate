package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    long id;
    @NotEmpty
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    long duration;
}
