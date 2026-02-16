package ru.practicum.ewm.error;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(/*List<StackTraceElement> errors,*/
                       String message,
                       String reason,
                       HttpStatus status,
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                       LocalDateTime timestamp) {
}
