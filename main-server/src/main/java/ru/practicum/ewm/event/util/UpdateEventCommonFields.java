package ru.practicum.ewm.event.util;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.util.DateTimePattern;
import ru.practicum.ewm.validation.StringLength;

import java.time.LocalDateTime;

import static ru.practicum.ewm.event.dto.StringLengthValues.*;
import static ru.practicum.ewm.event.dto.StringLengthValues.DESCRIPTION_MAX_LENGTH;

@Data
@Builder
public class UpdateEventCommonFields {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
