package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public class RequestDtoMapper {
    public static ParticipationRequestDto toParticipantRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toParticipantRequestDto(List<Request> requests) {
        return requests.stream().map(RequestDtoMapper::toParticipantRequestDto).toList();
    }
}
