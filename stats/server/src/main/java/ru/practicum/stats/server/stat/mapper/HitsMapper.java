package ru.practicum.stats.server.stat.mapper;

import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.server.stat.model.Hit;

import java.time.LocalDateTime;

public class HitsMapper {
    public static Hit toHit(CreateHitDto dto) {
        Hit hit = new Hit();

        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(LocalDateTime.now());

        return hit;
    }
}
