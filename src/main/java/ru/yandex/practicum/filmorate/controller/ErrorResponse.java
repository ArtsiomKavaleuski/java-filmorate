package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
class ErrorResponse {
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}