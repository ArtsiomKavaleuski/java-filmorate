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
    //уже существующие рейтинги MPA, добавлены в БД через файл data.sql
    private final MPA g = new MPA(1, "G");
    private final MPA pg = new MPA(2, "PG");
    private final MPA pg13 = new MPA(3, "PG-13");
    private final MPA r = new MPA(4, "R");
    private final MPA nc17 = new MPA(5, "NC-17");

    @Test
    public void shouldReturnCorrectGenre() {
        Assertions.assertEquals(g, mpaService.getMpaById(1));
        Assertions.assertEquals(pg, mpaService.getMpaById(2));
        Assertions.assertEquals(pg13, mpaService.getMpaById(3));
        Assertions.assertEquals(r, mpaService.getMpaById(4));
        Assertions.assertEquals(nc17, mpaService.getMpaById(5));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetGenreByIncorrectId() {
        Assertions.assertThrows(NotFoundException.class, () -> mpaService.getMpaById(134));
        Assertions.assertThrows(NotFoundException.class, () -> mpaService.getMpaById(0));
        Assertions.assertThrows(NotFoundException.class, () -> mpaService.getMpaById(6));
    }

    @Test
    public void shouldReturnCollectionOfGenresWithAllGenres() {
        Assertions.assertTrue(mpaService.getAll().contains(g));
        Assertions.assertTrue(mpaService.getAll().contains(pg));
        Assertions.assertTrue(mpaService.getAll().contains(pg13));
        Assertions.assertTrue(mpaService.getAll().contains(r));
        Assertions.assertTrue(mpaService.getAll().contains(nc17));
        Assertions.assertEquals(5, mpaService.getAll().size());
    }
}
