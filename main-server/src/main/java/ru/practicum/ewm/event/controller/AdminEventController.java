package ru.practicum.ewm.event.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.util.FindAllRequestParams;
import ru.practicum.ewm.util.DateTimePattern;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> findAll(
            @RequestParam(required = false) Long[] users,
            @RequestParam(required = false) State[] states,
            @RequestParam(required = false) Long[] categories,
            @DateTimeFormat(pattern = DateTimePattern.PATTERN)
            @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = DateTimePattern.PATTERN)
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        FindAllRequestParams requestParams = FindAllRequestParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        log.info("GET all events, params: {}", requestParams.toString());
        List<EventFullDto> res = eventService.findAllAdmin(requestParams);
        log.info("events found");
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(
            @PositiveOrZero @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updEvent
    ) {
        log.info("PATCH event, id={}", eventId);
        EventFullDto res = eventService.update(eventId, updEvent);
        log.info("event id={} patched", eventId);

        return ResponseEntity.ok(res);
    }
}
