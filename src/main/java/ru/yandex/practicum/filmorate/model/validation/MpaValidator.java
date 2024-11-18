package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.MPA;

public class MpaValidator implements ConstraintValidator<MpaCheck, MPA> {

    @Override
    public boolean isValid(MPA mpa, ConstraintValidatorContext context) {
        if (mpa != null) {
            return mpa.getId() > 0 && mpa.getId() < 6;
        }
        return false;
    }
}
