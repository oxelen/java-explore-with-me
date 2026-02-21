package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.util.FindAllPublicParams;
import ru.practicum.ewm.event.util.FindAllRequestParams;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEvent);

    List<EventShortDto> findByUser(Long userId, int from, int size);

    EventFullDto findById(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updEvent);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updEvent);

    List<ParticipationRequestDto> getParticipantRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<EventFullDto> findAllAdmin(FindAllRequestParams params);

    List<EventShortDto> findAllPublic(FindAllPublicParams params, HttpServletRequest request);

    EventFullDto findByIdPublic(Long id, HttpServletRequest request);
}
