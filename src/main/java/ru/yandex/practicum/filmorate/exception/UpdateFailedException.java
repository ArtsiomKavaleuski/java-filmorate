package ru.yandex.practicum.filmorate.exception;

public class UpdateFailedException extends RuntimeException {
    public UpdateFailedException(String message) {
        super(message);
    }
}