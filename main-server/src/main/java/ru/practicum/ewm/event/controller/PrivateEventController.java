package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventService;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PositiveOrZero @PathVariable Long userId,
                                               @Valid @RequestBody NewEventDto newEvent) {
        log.info("CREATE event");
        EventFullDto res = eventService.create(userId, newEvent);
        log.info("event created");

        return ResponseEntity.status(201).body(res);
    }
}
