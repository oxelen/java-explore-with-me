package ru.practicum.ewm.event.util;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.dto.Location;

import java.time.LocalDateTime;

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
