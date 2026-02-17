package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.util.DateTimePattern;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = DateTimePattern.PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private Status status;
}
