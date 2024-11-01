package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();

    public void addToFriends(long id) {
        friends.add(id);
    }

    public void removeFromFriends(long id) {
        friends.remove(id);
    }
}
