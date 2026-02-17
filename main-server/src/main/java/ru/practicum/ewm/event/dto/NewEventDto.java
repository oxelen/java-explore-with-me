package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.DateTimePattern;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

import java.time.LocalDateTime;

import static ru.practicum.ewm.event.dto.StringLengthValues.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = ANNOTATION_MIN_LENGTH, max = ANNOTATION_MAX_LENGTH)
    private String annotation;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Integer category;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotNull(message = ValidationMessages.NOT_NULL)
    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Location location;

    private boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private boolean requestModeration = true;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
    private String title;
}
