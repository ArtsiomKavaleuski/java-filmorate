package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateGenreServiceTest {
    private final GenreService genreService;
    private final Genre comedy = new Genre(1, "Комедия");
    private final Genre drama = new Genre(2, "Драма");
    private final Genre cartoon = new Genre(3, "Мультфильм");
    private final Genre thriller = new Genre(4, "Триллер");
    private final Genre doc = new Genre(5, "Документальный");
    private final Genre action = new Genre(6, "Боевик");

    @Test
    public void shouldReturnGenreWithId1() {
        Assertions.assertEquals(comedy, genreService.getGenreById(1));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGenreIdDoesntExists() {
        Assertions.assertThrows(NotFoundException.class,
                () -> genreService.getGenreById(134));
    }

    @Test
    public void shouldReturnCollectionOfGenresWithAllGenres() {
            Assertions.assertTrue(genreService.getAll().contains(comedy));
            Assertions.assertTrue(genreService.getAll().contains(drama));
            Assertions.assertTrue(genreService.getAll().contains(cartoon));
            Assertions.assertTrue(genreService.getAll().contains(thriller));
            Assertions.assertTrue(genreService.getAll().contains(doc));
            Assertions.assertTrue(genreService.getAll().contains(action));
    }
}
