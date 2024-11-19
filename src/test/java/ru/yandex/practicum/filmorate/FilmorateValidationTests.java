package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
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

    Film film = new Film(1, "film", "daescription",
            LocalDate.of(2000,1,1), 100,
            new MPA(1, "G"), new HashSet<>(), 0);

    public User getRandomUser() {
        String email = RandomString.make(7) + "@gmail.com";
        String login = RandomString.make(7);
        String name = RandomString.make(7);
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        return new User(email, login, name, birthday);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsEmpty() {
        User user = getRandomUser();
        user.setEmail("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserBirthdayIsInFuture() {
        User user = getRandomUser();
        user.setBirthday(LocalDate.of(2033, 1, 1));
        Assertions.assertThrows(ConstraintViolationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginIsEmpty() {
        User user = getRandomUser();
        user.setLogin("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginContainsWhitespaces() {
        User user = getRandomUser();
        user.setLogin("artemyc 333");
        Assertions.assertThrows(ConstraintViolationException.class, () -> userController.create(user));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUserIsCorrect() {
        User user = getRandomUser();
        Assertions.assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUserNameIsEmpty() {
        User user = getRandomUser();
        user.setName("");
        Assertions.assertDoesNotThrow(() -> userController.create(user));
        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmNameIsEmpty() {
        film.setName("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmDescriptionLengthIsTooBig() {
        film.setDescription("1".repeat(201));
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmReleaseDateIsBefore28Dec1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmDurationIsNegative() {
        film.setDuration(-1);
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.create(film));
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
        Assertions.assertThrows(ConstraintViolationException.class, () -> filmController.update(film));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenUpdateUserWithCorrectFields() {
        User user = getRandomUser();
        userController.create(user);
        user.setName("Anon");
        Assertions.assertDoesNotThrow(() -> userController.update(user));
        Assertions.assertTrue(userController.getAll().contains(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateUserWithIncorrectFields() {
        User user = getRandomUser();
        user = userController.create(user);
        user.setEmail("");
        User finalUser = user;
        Assertions.assertThrows(ConstraintViolationException.class, () -> userController.update(finalUser));
    }
}
