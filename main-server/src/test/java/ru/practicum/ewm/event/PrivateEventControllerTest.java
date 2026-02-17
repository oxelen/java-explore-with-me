package ru.practicum.ewm.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.event.controller.PrivateEventController;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.StateActionUser;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateEventController.class)
public class PrivateEventControllerTest {
    private static final String API_PREFIX = "/users/1/events";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    EventService service;

    private NewEventDto newEvent;
    private UpdateEventUserRequest updUser;

    @BeforeEach
    public void setup() {
        newEvent = NewEventDto.builder()
                .annotation("default test annotation")
                .category(1)
                .description("default test description")
                .eventDate(LocalDateTime.MAX)
                .location(new Location(1.1f, 1.1f))
                .paid(false)
                .participantLimit(5)
                .requestModeration(false)
                .title("test")
                .build();

        updUser = UpdateEventUserRequest.builder()
                .annotation("test update Annotation")
                .category(1)
                .description("test update Description")
                .eventDate(LocalDateTime.now().plusHours(4))
                .location(new Location(1.2f, 1.2f))
                .paid(true)
                .participantLimit(10)
                .requestModeration(true)
                .stateAction(StateActionUser.CANCEL_REVIEW)
                .title("test update title")
                .build();
    }

    @Test
    public void createTest() throws Exception {
        when(service.create(anyLong(), any())).thenReturn(null);

        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newEvent))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    public void findByIdTest() throws Exception {
        when(service.findById(anyLong(), anyLong())).thenReturn(null);

        mvc.perform(get(API_PREFIX + "/1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200));
    }

    @Test
    public void patchTest() throws Exception {
        when(service.update(anyLong(), anyLong(), any()))
                .thenReturn(null);

        mvc.perform(patch(API_PREFIX + "/1")
                        .content(mapper.writeValueAsString(updUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
