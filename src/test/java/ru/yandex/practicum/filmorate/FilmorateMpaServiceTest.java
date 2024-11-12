package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateMpaServiceTest {
    private final MpaService mpaService;
    private final MPA G = new MPA(1, "G");
    private final MPA PG = new MPA(2, "PG");
    private final MPA PG13 = new MPA(3, "PG-13");
    private final MPA R = new MPA(4, "R");
    private final MPA NC17 = new MPA(5, "NC-17");


    @Test
    public void shouldReturnCorrectGenre() {
        Assertions.assertEquals(G, mpaService.getMpaById(1));
        Assertions.assertEquals(PG, mpaService.getMpaById(2));
        Assertions.assertEquals(PG13, mpaService.getMpaById(3));
        Assertions.assertEquals(R, mpaService.getMpaById(4));
        Assertions.assertEquals(NC17, mpaService.getMpaById(5));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetGenreByIncorrectId() {
        Assertions.assertThrows(NotFoundException.class,
                () -> mpaService.getMpaById(134));
    }

    @Test
    public void shouldReturnCollectionOfGenresWithAllGenres() {
        Assertions.assertTrue(mpaService.getAll().contains(G));
        Assertions.assertTrue(mpaService.getAll().contains(PG));
        Assertions.assertTrue(mpaService.getAll().contains(PG13));
        Assertions.assertTrue(mpaService.getAll().contains(R));
        Assertions.assertTrue(mpaService.getAll().contains(NC17));
        Assertions.assertTrue(mpaService.getAll().size() == 5);
    }
}
