package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.util.DateTimePattern;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @DateTimeFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private Status status;
}
