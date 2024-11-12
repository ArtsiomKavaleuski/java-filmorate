package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateFilmServiceTest {
    private final FilmService filmService;
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
        Film response = filmService.create(testFilm);
        Assertions.assertEquals(response, filmService.getFilmById(response.getId()));
    }
}
