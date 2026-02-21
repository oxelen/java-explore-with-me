package ru.practicum.ewm.request.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestControllerPrivate {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(
            @PositiveOrZero @PathVariable Long userId,
            @NotNull @PositiveOrZero @RequestParam Long eventId
    ) {
        log.info("CREATE new request, userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto res = requestService.create(userId, eventId);
        log.info("request created id={}", res.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> findAll(@PositiveOrZero @PathVariable Long userId) {
        log.info("GET all requests, userId={}", userId);
        List<ParticipationRequestDto> res = requestService.findAll(userId);
        log.info("requests found");

        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long requestId
    ) {
        log.info("PATCH cancel request, userId={}, requestId={}", userId, requestId);
        ParticipationRequestDto res = requestService.cancelRequest(userId, requestId);
        log.info("request canceled, userId={}, requestId={}", userId, requestId);

        return ResponseEntity.ok(res);
    }
}
