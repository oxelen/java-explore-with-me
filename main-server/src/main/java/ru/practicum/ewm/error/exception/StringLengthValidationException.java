package ru.practicum.ewm.error.exception;

public class StringLengthValidationException extends RuntimeException {
    public StringLengthValidationException(String msg) {
        super(msg);
    }
}