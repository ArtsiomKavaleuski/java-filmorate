package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateFilmServiceTest {
    private final FilmService filmService;
    private final UserService userService;
    //уже существующие фильмы, добавлены в БД через файл data.sql
    private final Film interstellar = new Film(1, "Interstellar", "Space odissey",
            LocalDate.of(2018, 1, 2), 130,
            new MPA(2, "PG"), new HashSet<>(), 0);
    private final Film avatar = new Film(2, "Avatar", "alternative universe",
            LocalDate.of(2008, 1, 2), 120,
            new MPA(1, "G"), new HashSet<>(), 0);
    private final Film gladiator = new Film(3, "Gladiator", "history about Maximus",
            LocalDate.of(2001, 2, 20), 110,
            new MPA(4, "R"), new HashSet<>(), 0);

    private Film getRandomFilm() {
        String name = RandomString.make(7);
        String description = RandomString.make(7);
        LocalDate releaseDate = LocalDate.of(2000, 1, 1);
        long duration = 150;
        MPA mpa = new MPA(1, "G");
        return new Film(name, description, releaseDate, duration, mpa);
    }

    private User getRandomUser() {
        String email = RandomString.make(7) + "@gmail.com";
        String login = RandomString.make(7);
        String name = RandomString.make(7);
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        return new User(email, login, name, birthday);
    }

    @Test
    public void shouldReturnCorrectFilm() {
        Assertions.assertEquals(interstellar, filmService.getFilmById(interstellar.getId()));
        Assertions.assertEquals(avatar, filmService.getFilmById(2));
        Assertions.assertEquals(gladiator, filmService.getFilmById(3));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetFilmByIncorrectId() {
        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilmById(999));
    }

    @Test
    public void shouldReturnCollectionOfFilms() {
        Assertions.assertTrue(filmService.getAll().contains(interstellar));
        Assertions.assertTrue(filmService.getAll().contains(avatar));
        Assertions.assertTrue(filmService.getAll().contains(gladiator));
    }

    @Test
    public void shouldReturnCorrectFilmAfterCreating() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        Assertions.assertEquals(testFilm, filmService.getFilmById(testFilm.getId()));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectData() {
        Film testFilm = getRandomFilm();
        testFilm.setMpa(new MPA(8, "HH"));
        long lastFilmId = filmService.getAll().size();
        Assertions.assertThrows(ValidationException.class, () -> filmService.create(testFilm));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilmById(lastFilmId + 1));
    }

    @Test
    public void shouldUpdateExistingFilm() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        Assertions.assertEquals(testFilm, filmService.getFilmById(testFilm.getId()));
        testFilm.setName("updatedName");
        filmService.update(testFilm);
        Assertions.assertEquals(testFilm, filmService.getFilmById(testFilm.getId()));
        Assertions.assertEquals("updatedName", filmService.getFilmById(testFilm.getId()).getName());
    }

    @Test
    public void shouldNotUpdateFilmWithIncorrectData() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        Assertions.assertEquals(testFilm, filmService.getFilmById(testFilm.getId()));
        testFilm.setMpa(new MPA(8, "HH"));
        Film updatedFilm = testFilm;
        Assertions.assertThrows(ValidationException.class, () -> filmService.update(updatedFilm));
        Assertions.assertNotEquals(updatedFilm, filmService.getFilmById(updatedFilm.getId()));
    }

    @Test
    public void shouldAddLike() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        User testUser = getRandomUser();
        testUser = userService.create(testUser);
        long filmId = testFilm.getId();
        long userId = testUser.getId();
        Assertions.assertEquals(0, filmService.getFilmById(filmId).getLikes());
        Assertions.assertDoesNotThrow(() -> filmService.addLike(filmId, userId));
        Assertions.assertEquals(1, filmService.getFilmById(filmId).getLikes());
    }

    @Test
    public void shouldNotAddLikeIfFilmOrUserNotFound() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        User testUser = getRandomUser();
        testUser = userService.create(testUser);
        long filmId = testFilm.getId();
        long userId = testUser.getId();
        long wrongUserId = 999;
        long wrongFilmId = 888;
        Assertions.assertThrows(NotFoundException.class, () -> filmService.addLike(wrongFilmId, userId));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.addLike(filmId, wrongUserId));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.addLike(wrongFilmId, wrongUserId));
    }

    @Test
    public void shouldRemoveLike() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        User testUser = getRandomUser();
        testUser = userService.create(testUser);
        long filmId = testFilm.getId();
        long userId = testUser.getId();
        Assertions.assertDoesNotThrow(() -> filmService.addLike(filmId, userId));
        Assertions.assertEquals(1, filmService.getFilmById(filmId).getLikes());
        Assertions.assertDoesNotThrow(() -> filmService.removeLike(filmId, userId));
        Assertions.assertEquals(0, filmService.getFilmById(filmId).getLikes());
    }

    @Test
    public void shouldNotRemoveLikeIfFilmOrUserNotFound() {
        Film testFilm = getRandomFilm();
        testFilm = filmService.create(testFilm);
        User testUser = getRandomUser();
        testUser = userService.create(testUser);
        long filmId = testFilm.getId();
        long userId = testUser.getId();
        long wrongUserId = 999;
        long wrongFilmId = 888;
        Assertions.assertDoesNotThrow(() -> filmService.addLike(filmId, userId));
        Assertions.assertEquals(1, filmService.getFilmById(filmId).getLikes());
        Assertions.assertThrows(NotFoundException.class, () -> filmService.removeLike(wrongFilmId, userId));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.removeLike(filmId, wrongUserId));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.removeLike(wrongFilmId, wrongUserId));
        Assertions.assertEquals(1, filmService.getFilmById(filmId).getLikes());
    }
}
