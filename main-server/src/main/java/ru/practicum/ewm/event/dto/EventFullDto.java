package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.util.DateTimePattern;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EventFullDto {
    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private int participantLimit;

    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private int views;
}
