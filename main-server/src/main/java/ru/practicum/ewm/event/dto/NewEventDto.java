package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.util.DateTimePattern;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Integer category;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = 20, max = 7000)
    private String description;

    @NotNull(message = ValidationMessages.NOT_NULL)
    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Location location;

    private boolean paid = false;

    private Integer partLimit = 0;

    private boolean requestModeration = true;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = 3, max = 120)
    private String title;
}
