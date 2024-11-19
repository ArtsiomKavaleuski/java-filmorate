package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MPA {
    @Min(value = 1, message = "Индекс MPA рейтинга должен быть от 1 до 5")
    @Max(value = 5, message = "Индекс MPA рейтинга должен быть от 1 до 5")
    private long id;
    private String name;

    public MPA(long id) {
        this.id = id;
    }
}