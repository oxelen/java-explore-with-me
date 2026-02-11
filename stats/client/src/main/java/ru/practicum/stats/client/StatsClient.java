package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private final String appName;

    public StatsClient(@Value("${ewm-stats-server-url}") String url,
                       RestTemplateBuilder builder,
                       @Value("${spring.application.name}") String appName) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url)).requestFactory(()
                -> new HttpComponentsClientHttpRequestFactory()).build());
        this.appName = appName;
    }

    public ResponseEntity<Object> hit(CreateHitDto createHitDto) {
        log.info("Start client method hit");
        createHitDto.setApp(appName);

        return post("/hit", createHitDto);
    }

    public ResponseEntity<Object> findHits(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.info("Start client method findHits");
        Map<String, Object> params = Map.of("start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        return get("/stats", params);
    }
}
