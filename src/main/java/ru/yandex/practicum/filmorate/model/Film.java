package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.validation.MinimumDate;
import ru.yandex.practicum.filmorate.model.validation.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Comparable<Film> {
    private long id;
    @NotNull(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @MinimumDate(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private long duration;
    @Mpa(message = "MPA рейтинг с таким id не существует")
    private MPA mpa;
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