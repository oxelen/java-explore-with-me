package ru.practicum.stats.server.stat.error.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}
