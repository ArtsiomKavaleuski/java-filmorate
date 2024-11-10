//package ru.yandex.practicum.filmorate;
//
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.controller.UserController;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.MPA;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//
//@SpringBootTest
//class FilmorateApplicationTests {
//
//	@Autowired
//	UserController userController;
//	User user = new User();
//
//	@Autowired
//	FilmController filmController;
//	Film film = new Film();
//
//	@Test
//	void contextLoads() {
//	}
//
//	@BeforeEach
//	void beforeEach() {
//		user.setId(1);
//		user.setName("user");
//		user.setLogin("user");
//		user.setEmail("koval@bff.by");
//		user.setBirthday(LocalDate.of(2000,01,01));
//		film.setId(1);
//		film.setName("film1");
//		film.setDescription("description");
//		film.setReleaseDate(LocalDate.of(2000,01,01));
//		film.setDuration(100);
//		MPA mpa = new MPA();
//		mpa.setId(1);
//		film.setMpa(mpa);
//	}
//
//	@AfterEach
//	void afterEach() {
//		user.setId(1);
//		user.setName("user");
//		user.setLogin("user");
//		user.setEmail("koval@bff.by");
//		user.setBirthday(LocalDate.of(2000,01,01));
//		film.setId(1);
//		film.setName("film1");
//		film.setDescription("description");
//		film.setReleaseDate(LocalDate.of(2000,01,01));
//		film.setDuration(100);
//		MPA mpa = new MPA();
//		mpa.setId(1);
//		film.setMpa(mpa);
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenUserEmailIsEmpty() {
//		user.setEmail("");
//		Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
//	}
//
//	@Test
//
//	void shouldThrowValidationExceptionWhenUserBirthdayIsInFuture() {
//		user.setBirthday(LocalDate.of(2033,01,01));
//		Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenUserLoginIsEmpty() {
//		user.setLogin("");
//		Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenUserLoginContainsWhitespaces() {
//		user.setLogin("artemyc 333");
//		Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
//	}
//
//	@Test
//	void shouldNotThrowValidationExceptionWhenUserIsCorrect() {
//		Assertions.assertDoesNotThrow(() -> userController.create(user));
//	}
//
//	@Test
//	void shouldNotThrowValidationExceptionWhenUserNameIsEmpty() {
//		user.setName("");
//		Assertions.assertDoesNotThrow(() -> userController.create(user));
//		Assertions.assertTrue(user.getName().equals(user.getLogin()));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenFilmNameIsEmpty() {
//		film.setName("");
//		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenFilmDescriptionLengthIsTooBig() {
//		film.setDescription("1".repeat(201));
//		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenFilmReleaseDateIsBefore28Dec1895() {
//		film.setReleaseDate(LocalDate.of(1895, 12,27));
//		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenFilmDurationIsNegative() {
//		film.setDuration(-1);
//		Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
//	}
//
//	@Test
//	void shouldNotThrowValidationExceptionWhenFilmFieldsAreCorrect() {
//		Assertions.assertDoesNotThrow(() -> filmController.create(film));
//	}
//
//	@Test
//	void shouldNotThrowValidationExceptionWhenUpdateFilmWithCorrectFields() {
//		filmController.create(film);
//		film.setDuration(200);
//		Assertions.assertDoesNotThrow(() -> filmController.update(film));
//		Assertions.assertTrue(filmController.getAll().contains(film));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenUpdateFilmWithIncorrectFields() {
//		filmController.create(film);
//		film.setDuration(-100);
//		Assertions.assertThrows(ValidationException.class,() -> filmController.update(film));
//	}
//
//	@Test
//	void shouldNotThrowValidationExceptionWhenUpdateUserWithCorrectFields() {
//		userController.create(user);
//		user.setName("Anon");
//		Assertions.assertDoesNotThrow(() -> userController.update(user));
//		Assertions.assertTrue(userController.getAll().contains(user));
//	}
//
//	@Test
//	void shouldThrowValidationExceptionWhenUpdateUserWithIncorrectFields() {
//		userController.create(user);
//		user.setEmail("");
//		Assertions.assertThrows(ValidationException.class, () -> userController.update(user));
//	}
//
//
//
//
//
//
//}
