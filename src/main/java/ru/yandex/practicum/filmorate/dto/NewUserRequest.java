package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class NewUserRequest {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}