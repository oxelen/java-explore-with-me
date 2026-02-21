package ru.practicum.ewm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.stats.client.StatsClient;

@SpringBootTest
public class EwmServerTest {
    @MockBean
    private StatsClient statsClient;

    @Test
    public void contextLoads() {
    }
}
