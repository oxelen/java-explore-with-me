package ru.practicum.stats.server.stat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.dto.ResponseHitDto;
import ru.practicum.stats.server.stat.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.stats.dto.ResponseHitDto(" +
            "h.app, h.uri, count(distinct h.ip)" +
            ") " +
            "from Hit h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri")
    List<ResponseHitDto> findUniqueHits(LocalDateTime start, LocalDateTime end, Collection<String> uris);

    @Query("select new ru.practicum.stats.dto.ResponseHitDto(" +
            "h.app, h.uri, count(h.ip)" +
            ") " +
            "from Hit h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri")
    List<ResponseHitDto> findHits(LocalDateTime start, LocalDateTime end, Collection<String> uris);
}
