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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateUserServiceTest {
    private final UserService userService;
    //уже существующие пользователи, были добавлены в БД через файл data.sql
    private final User artem = new User(1, "koval@bff.by", "koval", "Artem",
            LocalDate.of(1994, 4, 5), new HashSet<>());
    private final User yauheni = new User(2, "kopylov@bff.by", "kopylov", "Yauheni",
            LocalDate.of(1992, 2, 28), new HashSet<>());
    private final User siarhei = new User(3, "vitushko@bff.by", "sd", "Siarhei",
            LocalDate.of(1945, 7, 13), new HashSet<>());
    private final User ihar = new User(4, "tsapliuk@bff.by", "tsapliuk", "Ihar",
            LocalDate.of(1968, 11, 3), new HashSet<>());

    public User getRandomUser() {
        String email = RandomString.make(7) + "@gmail.com";
        String login = RandomString.make(7);
        String name = RandomString.make(7);
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        return new User(email, login, name, birthday);
    }

    @Test
    public void shouldReturnCorrectCollectionOfUsers() {
        Assertions.assertTrue(userService.getAll().contains(artem));
        Assertions.assertTrue(userService.getAll().contains(yauheni));
        Assertions.assertTrue(userService.getAll().contains(siarhei));
        Assertions.assertTrue(userService.getAll().contains(ihar));
    }

    @Test
    public void shouldReturnCorrectUserById() {
        Assertions.assertEquals(artem, userService.getUserById(artem.getId()));
        Assertions.assertEquals(yauheni, userService.getUserById(yauheni.getId()));
        Assertions.assertEquals(siarhei, userService.getUserById(siarhei.getId()));
        Assertions.assertEquals(ihar, userService.getUserById(ihar.getId()));
    }

    @Test
    public void shouldNotReturnUserByIncorrectId() {
        long incorrectId = 999;
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(incorrectId));
    }

    @Test
    public void shouldAddUserToDbAfterCreating() {
        User testUser = getRandomUser();
        testUser = userService.create(testUser);
        Assertions.assertEquals(testUser, userService.getUserById(testUser.getId()));
    }

    @Test
    public void shouldReturnExceptionWhenCreatingUserWithIncorrectData() {
        User testUser = getRandomUser();
        testUser.setEmail("ghjhjg");
        Assertions.assertThrows(ValidationException.class, () -> userService.create(testUser));
    }

    @Test
    public void shouldNotSaveInDbUserWithIncorrectData() {
        User testUser = getRandomUser();
        testUser.setLogin("one one");
        long testUserExpectedId = userService.getAll().size() + 1;
        Assertions.assertThrows(ValidationException.class, () -> userService.create(testUser));
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(testUserExpectedId));
    }

    @Test
    public void shouldUpdateUser() {
        User testUser = getRandomUser();
        userService.create(testUser);
        Assertions.assertEquals(testUser, userService.getUserById(testUser.getId()));
        testUser.setName("updatedUser");
        userService.update(testUser);
        Assertions.assertEquals(testUser, userService.getUserById(testUser.getId()));
    }

    @Test
    public void shouldNotUpdateUserWithIncorrectData() {
        User testUser = getRandomUser();
        userService.create(testUser);
        Assertions.assertEquals(testUser, userService.getUserById(testUser.getId()));
        testUser.setLogin("updated User");
        Assertions.assertThrows(ValidationException.class, () -> userService.update(testUser));
        Assertions.assertNotEquals(testUser, userService.getUserById(testUser.getId()));
    }

    @Test
    public void shouldAddFriend() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        Assertions.assertEquals(0, userService.getUserById(testUser.getId()).getFriends().size());
        Assertions.assertEquals(0, userService.getUserById(testUserTwo.getId()).getFriends().size());
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        Assertions.assertEquals(1, userService.getUserById(testUser.getId()).getFriends().size());
    }

    @Test
    public void shouldAddFriendOnlyToFirstUser() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        Assertions.assertEquals(0, userService.getUserById(testUser.getId()).getFriends().size());
        Assertions.assertEquals(0, userService.getUserById(testUserTwo.getId()).getFriends().size());
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        Assertions.assertEquals(1, userService.getUserById(testUser.getId()).getFriends().size());
        Assertions.assertEquals(0, userService.getUserById(testUserTwo.getId()).getFriends().size());
    }

    @Test
    public void shouldReturnExceptionWhenAddFriendOrUserWithIncorrectId() {
        long incorrectUserId = 999;
        long incorrectFriendId = 888;
        long userId = artem.getId();
        long friendId = yauheni.getId();
        Assertions.assertThrows(NotFoundException.class, () -> userService.addFriend(userId, incorrectFriendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.addFriend(incorrectUserId, friendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.addFriend(incorrectUserId, incorrectFriendId));
    }

    @Test
    public void shouldRemoveFriend() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        Assertions.assertEquals(1, userService.getUserById(testUser.getId()).getFriends().size());
        userService.removeFriend(testUser.getId(), testUserTwo.getId());
        Assertions.assertEquals(0, userService.getUserById(testUser.getId()).getFriends().size());
    }

    @Test
    public void shouldReturnExceptionWhenRemoveFriendOrUserWithIncorrectId() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        long incorrectUserId = 999;
        long incorrectFriendId = 888;
        long userId = testUser.getId();
        long friendId = testUserTwo.getId();
        userService.addFriend(userId, friendId);
        Assertions.assertThrows(NotFoundException.class, () -> userService.removeFriend(userId, incorrectFriendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.removeFriend(incorrectUserId, friendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.removeFriend(incorrectUserId, incorrectFriendId));
    }

    @Test
    public void shouldReturnCollectionOfUserFriends() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        User testUserThree = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        testUserThree = userService.create(testUserThree);
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        userService.addFriend(testUser.getId(), testUserThree.getId());
        Assertions.assertEquals(2, userService.getAllFriends(testUser.getId()).size());
        Assertions.assertTrue(userService.getAllFriends(testUser.getId()).contains(testUserTwo));
        Assertions.assertTrue(userService.getAllFriends(testUser.getId()).contains(testUserThree));
    }

    @Test
    public void shouldThrowExceptionWhenTryingGetUserFriendsByIncorrectId() {
        long incorrectUserId = 999;
        Assertions.assertThrows(NotFoundException.class, () -> userService.getAllFriends(incorrectUserId));
    }

    @Test
    public void shouldReturnCollectionOfCommonFriends() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        User testUserThree = getRandomUser();
        User testUserFour = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        testUserThree = userService.create(testUserThree);
        testUserFour = userService.create(testUserFour);
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        userService.addFriend(testUser.getId(), testUserThree.getId());
        userService.addFriend(testUserFour.getId(), testUserTwo.getId());
        Assertions.assertTrue(userService.getCommonFriends(testUser.getId(), testUserFour.getId()).contains(testUserTwo));
        Assertions.assertEquals(1, userService.getCommonFriends(testUser.getId(), testUserFour.getId()).size());
    }

    @Test
    public void shouldReturnEmptyCollectionIfNoCommonFriends() {
        User testUser = getRandomUser();
        User testUserTwo = getRandomUser();
        User testUserThree = getRandomUser();
        User testUserFour = getRandomUser();
        testUser = userService.create(testUser);
        testUserTwo = userService.create(testUserTwo);
        testUserThree = userService.create(testUserThree);
        testUserFour = userService.create(testUserFour);
        userService.addFriend(testUser.getId(), testUserTwo.getId());
        userService.addFriend(testUser.getId(), testUserThree.getId());
        userService.addFriend(testUserFour.getId(), testUser.getId());
        Assertions.assertTrue(userService.getCommonFriends(testUser.getId(), testUserFour.getId()).isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenGetCommonFriendsWithIncorrectIds() {
        long incorrectUserId = 999;
        long incorrectFriendId = 888;
        long userId = artem.getId();
        long friendId = yauheni.getId();
        userService.addFriend(userId, friendId);
        Assertions.assertThrows(NotFoundException.class, () -> userService.getCommonFriends(userId, incorrectFriendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.getCommonFriends(incorrectUserId, friendId));
        Assertions.assertThrows(NotFoundException.class, () -> userService.getCommonFriends(incorrectUserId, incorrectFriendId));
    }
}
