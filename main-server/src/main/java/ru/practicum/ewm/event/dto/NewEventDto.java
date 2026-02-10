package ru.practicum.ewm.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.util.DateTimePattern;
import ru.practicum.ewm.util.ValidationMessages;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private String annotation;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Integer category;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private String description;

    @NotNull(message = ValidationMessages.NOT_NULL)
    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Location location;

    private boolean paid;

    private Integer partLimit;

    private boolean requestModeration;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private String title;
}
