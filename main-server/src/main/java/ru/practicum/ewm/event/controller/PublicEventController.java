package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.util.FindAllPublicParams;
import ru.practicum.ewm.event.util.FindAllRequestParams;
import ru.practicum.ewm.event.util.SortFilters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> findAllPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortFilters sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        log.info("GET all events public");
        FindAllPublicParams params = FindAllPublicParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        List<EventShortDto> res = eventService.findAllPublic(params, request);
        log.info("events found");

        return ResponseEntity.ok(res);
    }
}
