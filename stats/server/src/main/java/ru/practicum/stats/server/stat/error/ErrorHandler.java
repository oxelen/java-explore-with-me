package ru.practicum.stats.server.stat.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.server.stat.error.exception.ValidationException;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "Incorrectly made request",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );

        return ResponseEntity.status(error.status()).body(error);
    }
}
