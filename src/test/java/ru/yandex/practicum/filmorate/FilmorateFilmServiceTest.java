package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateFilmServiceTest {
    private final FilmService filmService;
    private final UserService userService;
    private final Film interstellar = new Film(1, "Interstellar", "Space odissey",
            LocalDate.of(2018,1,2), 130, new MPA(2, "PG"),
            Set.of(new Genre(2, "Драма"), new Genre(6, "Боевик")), 2);
    private final Film avatar = new Film(2, "Avatar", "alternative universe",
            LocalDate.of(2008,1,2), 120, new MPA(1, "G"),
            Set.of(new Genre(2, "Драма")), 3);
    private final Film gladiator = new Film(3, "Gladiator", "history about Maximus",
            LocalDate.of(2001,2,20), 110, new MPA(4, "R"),
            Set.of(new Genre(2, "Драма"), new Genre(6, "Боевик")), 1);

//    @AfterEach
//    public void afterEach() {
//
//    }

    @Test
    public void shouldReturnCorrectFilm() {
        Assertions.assertEquals(interstellar, filmService.getFilmById(1));
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
        Assertions.assertTrue(filmService.getAll().size() == 3);
    }

    @Test
    public void shouldReturnCorrectFilmAfterCreating() {
        Film testFilm = new Film("testFilm", "testFilm description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        Film createdFilm = filmService.create(testFilm);
        Assertions.assertEquals(createdFilm, filmService.getFilmById(createdFilm.getId()));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectData() {
        Film testFilm = new Film("testFilm", "testFilm description",
                LocalDate.of(2002, 1,5), 135, new MPA(8));
        long lastFilmId = filmService.getAll().size();
        Assertions.assertThrows(ValidationException.class, () -> filmService.create(testFilm));
        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilmById(lastFilmId + 1));
    }

    @Test
    public void shouldUpdateExistingFilm() {
        Film testFilm = new Film("filmBeforeUpdate", "description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        Film updatedTestFilm = new Film("updatedTestFilm", "description",
                LocalDate.of(2002, 1,5), 155, new MPA(1, "G"));
        testFilm = filmService.create(testFilm);
        updatedTestFilm.setId(testFilm.getId());
        filmService.update(updatedTestFilm);
        Assertions.assertEquals(filmService.getFilmById(testFilm.getId()), updatedTestFilm);
    }

    @Test
    public void shouldNotUpdateFilmWithIncorrectData() {
        Film testFilm = new Film("filmBeforeUpdate", "description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        Film updatedTestFilm = new Film("updatedTestFilm", "description",
                LocalDate.of(2002, 1,5), 155, new MPA(8, "G"));
        testFilm = filmService.create(testFilm);
        updatedTestFilm.setId(testFilm.getId());
        Assertions.assertThrows(ValidationException.class, () -> filmService.update(updatedTestFilm));
        Assertions.assertNotEquals(filmService.getFilmById(testFilm.getId()), updatedTestFilm);
    }

    @Test
    public void shouldAddLike() {
        Film testFilm = new Film("film", "description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        testFilm = filmService.create(testFilm);
        User testUser = new User("testUser@gmail.com", "testUserLogin", "testUser", LocalDate.of(1994, 4, 5));
        testUser = userService.create(testUser);
        long filmId = testFilm.getId();
        long userId = testUser.getId();
        Assertions.assertEquals(0, filmService.getFilmById(filmId).getLikes());
        Assertions.assertDoesNotThrow(() -> filmService.addLike(filmId, userId));
        Assertions.assertEquals(1, filmService.getFilmById(filmId).getLikes());
    }

    @Test
    public void shouldNotAddLikeIfFilmOrUserNotFound() {
        Film testFilm = new Film("film", "description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        testFilm = filmService.create(testFilm);
        User testUser = new User("testUser@gmail.com", "testUserLogin", "testUser", LocalDate.of(1994, 4, 5));
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
        Film testFilm = new Film("film", "description",
                LocalDate.of(2002, 1,5), 135, new MPA(1, "G"));
        testFilm = filmService.create(testFilm);
        User testUser = new User("testUser@gmail.com", "testUserLogin", "testUser", LocalDate.of(1994, 4, 5));
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
        Film testFilm = new Film("film", "description",
                LocalDate.of(2002, 1, 5), 135, new MPA(1, "G"));
        testFilm = filmService.create(testFilm);
        User testUser = new User("testUser@gmail.com", "testUserLogin", "testUser", LocalDate.of(1994, 4, 5));
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
