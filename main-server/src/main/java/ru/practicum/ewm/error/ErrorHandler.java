package ru.practicum.ewm.error;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.error.exception.ValidationException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final HttpStatus status =HttpStatus.BAD_REQUEST;
        List<ApiError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String defMessage = error.getDefaultMessage();
            Object value = ((FieldError) error).getRejectedValue();
            String strMsg = value == null ? "null" : value.toString();

            String msg = String.format("Field: %s. Error: %s. Value: %s", fieldName, defMessage, strMsg);

            errors.add(new ApiError(
                   /* Arrays.asList(ex.getStackTrace()),*/
                    msg,
                    "Incorrectly made request",
                    status,
                    LocalDateTime.now()
            ));
        });

        return ResponseEntity.status(status).body(errors);
    }

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleSQLException(ConstraintViolationException ex) {
        ApiError error = new ApiError(
           /* Arrays.asList(ex.getStackTrace()),*/
                ex.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );

        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<ApiError> handleConditionsNotMet(ConditionsNotMetException ex) {
        ApiError error = new ApiError(
                /* Arrays.asList(ex.getStackTrace()),*/
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );

        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );

        return ResponseEntity.status(error.status()).body(error);
    }
}
