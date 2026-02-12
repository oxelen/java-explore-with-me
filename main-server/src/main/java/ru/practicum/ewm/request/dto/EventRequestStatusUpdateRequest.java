package ru.practicum.ewm.request.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.util.ValidationMessages;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotEmpty(message = ValidationMessages.NOT_EMPTY)
    @NotNull(message = ValidationMessages.NOT_NULL)
    private List<Long> requestIds;

    @NotNull(message = ValidationMessages.NOT_NULL)
    private Status status;
}
