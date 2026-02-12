package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> create(
            @PositiveOrZero @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEvent
    ) {
        log.info("CREATE event");
        EventFullDto res = eventService.create(userId, newEvent);
        log.info("event created");

        return ResponseEntity.status(201).body(res);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> findByUser(
            @PositiveOrZero @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET events by user, id={}", userId);
        List<EventShortDto> res = eventService.findByUser(userId, from, size);
        log.info("events found");

        return ResponseEntity.status(200).body(res);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> findById(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long eventId
    ) {
        log.info("GET events by id, id={}", eventId);
        EventFullDto res = eventService.findById(userId, eventId);
        log.info("event found, id={}", eventId);

        return ResponseEntity.status(200).body(res);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updEvent
    ) {
        log.info("PATCH event, id={}", eventId);
        EventFullDto res = eventService.update(userId, eventId, updEvent);
        log.info("event updated, id={}", eventId);

        return ResponseEntity.status(200).body(res);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipantRequests(
            @PositiveOrZero
            @PathVariable
            Long userId,

            @PositiveOrZero
            @PathVariable
            Long eventId
    ) {
        log.info("GET participant requests, userId={}, eventId={}", userId, eventId);
        List<ParticipationRequestDto> res = eventService.getParticipantRequests(userId, eventId);
        log.info("requests found");

        return ResponseEntity.status(200).body(res);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> patchRequestStatus(
            @PositiveOrZero
            @PathVariable
            Long userId,

            @PositiveOrZero
            @PathVariable
            Long eventId,

            @Valid
            @RequestBody
            EventRequestStatusUpdateRequest request
    ) {
        log.info("PATCH request status, userId={}, eventId={}", userId, eventId);
        EventRequestStatusUpdateResult res = eventService.patchRequestStatus(userId, eventId, request);
        log.info("status updated");

        return ResponseEntity.status(200).body(res);
    }
}
