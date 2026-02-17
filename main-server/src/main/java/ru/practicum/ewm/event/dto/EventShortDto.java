package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.util.DateTimePattern;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
