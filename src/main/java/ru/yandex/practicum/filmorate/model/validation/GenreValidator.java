package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.Set;

public class GenreValidator implements ConstraintValidator<GenreCheck, Set<Genre>> {

    @Override
    public boolean isValid(Set<Genre> genres, ConstraintValidatorContext context) {
        boolean result = true;
        if (!genres.isEmpty()) {
            for(Genre genre : genres) {
                if (genre.getId() < 1 || genre.getId() > 6) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
