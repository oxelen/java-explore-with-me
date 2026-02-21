package ru.practicum.stats.server.stat.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
                       String message,
                       String reason,
                       HttpStatus status,
                       LocalDateTime timestamp
) {
}
