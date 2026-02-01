package ru.practicum.stats.server.stat.service;

import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void hit(CreateHitDto createHitDto);

    List<ResponseHitDto> findHits(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
