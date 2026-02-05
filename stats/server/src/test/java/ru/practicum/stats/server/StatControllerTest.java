package ru.practicum.stats.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;
import ru.practicum.stats.server.stat.controller.StatController;
import ru.practicum.stats.server.stat.service.StatService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatController.class)
public class StatControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StatService statService;

    @Autowired
    MockMvc mvc;

    CreateHitDto createDto;
    ResponseHitDto responseDto;

    @BeforeEach
    public void setup() {
        createDto = CreateHitDto.builder()
                .app("app")
                .ip("ip")
                .uri("uri")
                .build();

        responseDto = ResponseHitDto.builder()
                .app("app")
                .hits(1L)
                .uri("uri")
                .build();
    }

    @Test
    public void hitTest() throws Exception {
        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    public void getTest() throws Exception {
        when(statService.findHits(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(responseDto));

        mvc.perform(get("/stats?start=2020-01-01 00:00:00&end=2020-01-02 00:00:00")
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].app").value(responseDto.getApp()))
                .andExpect(jsonPath("$[0].hits").value(responseDto.getHits()))
                .andExpect(jsonPath("$[0].uri").value(responseDto.getUri()));
    }
}
