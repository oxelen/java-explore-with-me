package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.util.DateTimePattern;
import ru.practicum.ewm.validation.StringLength;

import java.time.LocalDateTime;

import static ru.practicum.ewm.event.dto.StringLengthValues.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @StringLength(min = ANNOTATION_MIN_LENGTH, max = ANNOTATION_MAX_LENGTH)
    private String annotation;

    private Integer category;

    @StringLength(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    private StateActionUser stateAction;

    @StringLength(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
    private String title;
}
