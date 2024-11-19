package ru.yandex.practicum.filmorate.exception;

public class InsertFailedException extends  RuntimeException {
    public InsertFailedException(String message) {
        super(message);
    }
}

