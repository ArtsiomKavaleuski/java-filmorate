package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;
import ru.yandex.practicum.filmorate.model.MPA;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MpaValidator.class)
public @interface Mpa {
    String message() default "incorrect MPA id";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    //Class<? extends MPA> value();
}
