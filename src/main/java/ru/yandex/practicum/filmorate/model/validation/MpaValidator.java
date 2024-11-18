package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

public class MpaValidator implements ConstraintValidator<Mpa, MPA> {

//    @Override
//    public void initialize(Mpa constraintAnnotation) {
//        MPA mpa = constraintAnnotation.value();
//    }

    @Override
    public boolean isValid(MPA mpa, ConstraintValidatorContext context) {
        if (mpa != null) {
            return mpa.getId() > 0 && mpa.getId() < 6;
        }
        return false;
    }
}
