package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

//    @GetMapping("/{id}/friends")
//    public Collection<User> getAllFriends(@PathVariable("id") long id) {
//        return userService.getAllFriends(id);
//    }
//
//    @GetMapping("/{id}/friends/common/{otherId}")
//    public Collection<User> getCommonFriends(@PathVariable("id") long id,
//                                             @PathVariable("otherId") long otherId) {
//        return userService.getCommonFriends(id, otherId);
//    }

    @PostMapping
    public User create(@RequestBody NewUserRequest request) {
        log.info("Передан объект пользователя {} для добавления.", request);
        return userService.create(request);
    }

//    @PutMapping
//    public User update(@RequestBody User newUser) {
//        log.info("Передан модифицированный объект пользователя {} для обновления.", newUser);
//        return userService.update(newUser);
//    }

//    @PutMapping("/{id}/friends/{friendId}")
//    public User addToFriends(@PathVariable("id") long id,
//                                       @PathVariable("friendId") long friendId) {
//        log.info("Передан id = {} для добавления в друзья пользователю с id = {}", friendId, id);
//        return userService.addFriend(id, friendId);
//    }

//    @DeleteMapping("/{id}/friends/{friendId}")
//    public User removeFriend(@PathVariable("id") long id,
//                                       @PathVariable("friendId") long friendId) {
//        log.info("Передан id = {} для удаления из друзей пользователя с id = {}", friendId, id);
//        return userService.removeFriend(id, friendId);
//    }


}