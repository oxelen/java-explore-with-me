package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime createdOn;

    private String description;

    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit;

    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private int views;
}
