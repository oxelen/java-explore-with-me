package ru.practicum.stats.server.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/test")
@Profile("qa")
@RequiredArgsConstructor
@Slf4j
public class TestDataBaseController {
    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset() {
        //hitsRepository.deleteAll();
        jdbcTemplate.execute("DELETE FROM hits");
        jdbcTemplate.execute("ALTER TABLE hits ALTER COLUMN id RESTART WITH 1");
    }
}
