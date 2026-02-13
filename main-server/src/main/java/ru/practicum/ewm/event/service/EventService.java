package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.util.FindAllRequestParams;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEvent);

    List<EventShortDto> findByUser(Long userId, int from, int size);

    EventFullDto findById(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updEvent);

    List<ParticipationRequestDto> getParticipantRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<EventFullDto> findAllAdmin(FindAllRequestParams params);
}
