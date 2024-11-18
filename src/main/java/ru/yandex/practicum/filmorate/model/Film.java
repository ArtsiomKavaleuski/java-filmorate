package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.validation.GenreCheck;
import ru.yandex.practicum.filmorate.model.validation.MinimumDate;
import ru.yandex.practicum.filmorate.model.validation.MpaCheck;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Comparable<Film> {
    private long id;
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @MinimumDate(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private long duration;
    @MpaCheck(message = "MPA рейтинг должен быть указан и его id должен быть от 1 до 5")
    private MPA mpa;
    @GenreCheck(message = "Указан жанр с несуществующим id")
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