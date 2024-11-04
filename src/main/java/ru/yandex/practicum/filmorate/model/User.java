package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    Map<Long, Boolean> friends = new HashMap<>();


}
