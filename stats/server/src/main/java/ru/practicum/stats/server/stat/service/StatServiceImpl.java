package ru.practicum.stats.server.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;
import ru.practicum.stats.server.stat.dao.HitsRepository;
import ru.practicum.stats.server.stat.mapper.HitsMapper;
import ru.practicum.stats.server.stat.model.Hit;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final HitsRepository hitsRepository;

    @Override
    public void hit(CreateHitDto createHitDto) {
        Hit hit = HitsMapper.toHit(createHitDto);
        hitsRepository.save(hit);
    }

    @Override
    public List<ResponseHitDto> findHits(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Collection<String> urisList = uris == null
                ? hitsRepository.findAll().stream().map(Hit::getUri).toList()
                : Arrays.asList(uris);
        return unique ? hitsRepository.findUniqueHits(start, end, urisList)
                : hitsRepository.findHits(start, end, urisList);
    }
}
