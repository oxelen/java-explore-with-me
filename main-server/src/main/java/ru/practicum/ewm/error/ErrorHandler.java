package ru.practicum.ewm.error;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exception.NotFoundException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now()
            ));
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleSQLException(ConstraintViolationException ex) {
        return new ApiError(
           /* Arrays.asList(ex.getStackTrace()),*/
                ex.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundException(NotFoundException ex) {
        return new ApiError(
                ex.getMessage(),
                "The required object was not found.",
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }
}
