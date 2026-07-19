package ru.practicum.ewm.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.client.StatsClient;

@RestController
@Profile("qa")
@RequestMapping("/internal/test")
@RequiredArgsConstructor
@Slf4j
public class TestDatabaseController {
    private final JdbcTemplate jdbcTemplate;

    private final StatsClient statsClient;

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM compilations_events");
        jdbcTemplate.execute("DELETE FROM requests");
        jdbcTemplate.execute("DELETE FROM events");
        jdbcTemplate.execute("DELETE FROM compilations");
        jdbcTemplate.execute("DELETE FROM categories");
        jdbcTemplate.execute("DELETE FROM users");

        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE categories ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE compilations ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE events ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE requests ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1");

        statsClient.reset();
    }
}
