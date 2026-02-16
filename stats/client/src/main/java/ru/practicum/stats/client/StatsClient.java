package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient {
    private final String appName;
    private final RestTemplate rest;

    public StatsClient(@Value("${ewm.stats.server.url}") String url,
                       RestTemplateBuilder builder,
                       @Value("${spring.application.name}") String appName) {
        this.rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(url)).build();
        this.appName = appName;
    }

    public ResponseEntity<ResponseHitDto> hit(CreateHitDto createHitDto) {
        log.info("Start client method hit");
        createHitDto.setApp(appName);

        return rest.postForEntity("/hit", createHitDto, ResponseHitDto.class);
    }

    public ResponseEntity<List<ResponseHitDto>> findStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.info("Start client method findHits");
        Map<String, Object> params = Map.of("start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        return rest.exchange("/hits?end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseHitDto>>() {
                },
                params);
    }
}
