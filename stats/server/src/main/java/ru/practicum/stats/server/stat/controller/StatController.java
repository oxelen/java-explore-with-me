package ru.practicum.stats.server.stat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;
import ru.practicum.stats.server.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<ResponseHitDto> hit(@Valid @RequestBody CreateHitDto createHitDto) {
        log.info("POST create hit, uri: {}, ip: {}", createHitDto.getUri(), createHitDto.getIp());
        statService.hit(createHitDto);

        return ResponseEntity.status(201).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ResponseHitDto>> findStats(@RequestParam(name = "start")
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                          LocalDateTime start,
                                                          @RequestParam(name = "end")
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                          LocalDateTime end,
                                                          @RequestParam(name = "uris", required = false)
                                                          String[] uris,
                                                          @RequestParam(name = "unique", defaultValue = "false")
                                                          boolean unique) {
        log.info("GET findStats, start: {}, end: {}, unique = {}", start, end, unique);
        List<ResponseHitDto> result = statService.findHits(start, end, uris, unique);

        return ResponseEntity
                .status(200)
                .body(result);
    }
}
