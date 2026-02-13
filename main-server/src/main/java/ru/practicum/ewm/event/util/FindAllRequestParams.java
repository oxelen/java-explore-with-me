package ru.practicum.ewm.event.util;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.State;

import java.time.LocalDateTime;

@Data
@Builder
public class FindAllRequestParams {
    private Long[] users;
    private State[] states;
    private Long[] categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private int from;
    private int size;
}
