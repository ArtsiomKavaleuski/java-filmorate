package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateValidationTests {
    @Autowired
    UserController userController;
    @Autowired
    FilmController filmController;
    User user = new User(1, "test@gmail.com", "user", "user",
            LocalDate.of(2000,1,1), new HashSet<>());

    Film film = new Film(1, "film", "daescription",
            LocalDate.of(2000,1,1), 100,
            new MPA(1, "G"), new HashSet<>(), 0);

    @Test
    void contextLoads() {
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsEmpty() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserBirthdayIsInFuture() {
        user.setBirthday(LocalDate.of(2033, 1, 1));
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginIsEmpty() {
        user.setLogin("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginContainsWhitespaces() {
        user.setLogin("artemyc 333");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUserIsCorrect() {
        Assertions.assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUserNameIsEmpty() {
        user.setName("");
        Assertions.assertDoesNotThrow(() -> userController.create(user));
        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmNameIsEmpty() {
        film.setName("");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmDescriptionLengthIsTooBig() {
        film.setDescription("1".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmReleaseDateIsBefore28Dec1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmDurationIsNegative() {
        film.setDuration(-1);
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenFilmFieldsAreCorrect() {
        Assertions.assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUpdateFilmWithCorrectFields() {
        filmController.create(film);
        film.setDuration(200);
        Assertions.assertDoesNotThrow(() -> filmController.update(film));
        Assertions.assertTrue(filmController.getAll().contains(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateFilmWithIncorrectFields() {
        filmController.create(film);
        film.setDuration(-100);
        Assertions.assertThrows(ValidationException.class, () -> filmController.update(film));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUpdateUserWithCorrectFields() {
        userController.create(user);
        user.setName("Anon");
        Assertions.assertDoesNotThrow(() -> userController.update(user));
        Assertions.assertTrue(userController.getAll().contains(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithIncorrectFields() {
        userController.create(user);
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.update(user));
    }
}
