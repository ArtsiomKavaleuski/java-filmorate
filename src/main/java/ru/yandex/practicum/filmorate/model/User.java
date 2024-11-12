package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    Set<Friend> friends = new HashSet<>();

    public User(long id, String email, String login, String name) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
    }
}