package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
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
        return requests.stream().map(RequestMapper::toParticipantRequestDto).toList();
    }

    public static Request toRequest(User user, Event event) {
        Request req = new Request();
        req.setCreated(LocalDateTime.now());
        req.setEvent(event);
        req.setRequester(user);
        req.setStatus(Status.CONFIRMED);

        return req;
    }
}
