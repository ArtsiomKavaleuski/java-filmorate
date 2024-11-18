package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenreValidator.class)
public @interface GenreCheck {
    String message() default "incorrect Genre id";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
