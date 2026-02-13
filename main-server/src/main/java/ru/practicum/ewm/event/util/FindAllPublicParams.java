package ru.practicum.ewm.event.util;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Data
@Builder
public class FindAllPublicParams {
    private String text;
    private Long[] categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortFilters sort;
    private int from;
    private int size;
}
