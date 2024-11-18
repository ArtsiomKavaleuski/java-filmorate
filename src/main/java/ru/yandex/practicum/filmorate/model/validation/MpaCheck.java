package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MpaValidator.class)
public @interface MpaCheck {
    String message() default "incorrect MPA id";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
