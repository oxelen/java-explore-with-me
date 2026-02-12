package ru.practicum.ewm.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.EntityGetter;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class EventServiceImplTest {
    private final EntityManager em;
    private final EventService eventService;
    private final EntityGetter eg;

    @MockBean
    StatsClient statsClient;

    private Category cat;
    private User user;

    @BeforeEach
    public void setup() {
        cat = eg.insertCategory();
        user = eg.insertUser();
    }

    @Test
    public void createTest() {
        NewEventDto createDto = getDefaultDto();
        eventService.create(user.getId(), createDto);

        TypedQuery<Event> query = em.createQuery("select e from Event e where title = :title", Event.class);
        Event res = query.setParameter("title", createDto.getTitle()).getSingleResult();

        assertThat(res.getId(), notNullValue());
        assertThat(res.getCategory(), equalTo(cat));
        assertThat(res.getInitiator(), equalTo(user));
    }

    @Test
    public void eventDateShouldBefore2Hours() {
        NewEventDto createDto = getDefaultDto();
        createDto.setEventDate(LocalDateTime.now());

        Assertions.assertThrows(ConditionsNotMetException.class, () -> eventService.create(user.getId(), createDto));
    }

    @Test
    public void findByUserTest() {
        when(statsClient.findStats(any(), any(), any(), anyBoolean()))
                .thenReturn(ResponseEntity.status(200).body(new ArrayList<>()));

        NewEventDto createDto = getDefaultDto();
        eventService.create(user.getId(), createDto);

        List<EventShortDto> res = eventService.findByUser(user.getId(), 0, 10);
        assertThat(res, notNullValue());
        assertThat(res.size(), equalTo(1));
    }

    @Test
    public void findByIdTest() {
        when(statsClient.findStats(any(), any(), any(), anyBoolean()))
                .thenReturn(ResponseEntity.status(200).body(new ArrayList<>()));

        NewEventDto createDto = getDefaultDto();
        EventFullDto created = eventService.create(user.getId(), createDto);

        EventFullDto found =  eventService.findById(user.getId(), created.getId());

        assertThat(found, notNullValue());
        assertThat(found.getId(), equalTo(created.getId()));
    }

    @Test
    public void updateTest() {
        when(statsClient.findStats(any(), any(), any(), anyBoolean()))
                .thenReturn(ResponseEntity.status(200).body(new ArrayList<>()));

        NewEventDto createDto = getDefaultDto();
        EventFullDto created = eventService.create(user.getId(), createDto);

        UpdateEventUserRequest updDto = getUpdDto();
        EventFullDto updated = eventService.update(user.getId(), created.getId(), updDto);

        assertThat(updated, notNullValue());
        assertThat(updated.getId(), equalTo(created.getId()));
        assertThat(updated.getAnnotation(), equalTo(updDto.getAnnotation()));
    }

    private UpdateEventUserRequest getUpdDto() {
        return UpdateEventUserRequest.builder()
                .annotation("updAnnotation")
                .category(cat.getId().intValue())
                .description("updDescription")
                .eventDate(LocalDateTime.now().plusHours(4))
                .location(new Location(1.2f, 1.2f))
                .paid(true)
                .participantLimit(10)
                .requestModeration(true)
                .stateAction(StateActionUser.CANCEL_REVIEW)
                .title("updTitle")
                .build();
    }

    private NewEventDto getDefaultDto() {
        return NewEventDto.builder()
                .annotation("test")
                .category(cat.getId().intValue())
                .description("test")
                .eventDate(LocalDateTime.MAX)
                .location(new Location(1.1f, 1.1f))
                .paid(false)
                .partLimit(5)
                .requestModeration(false)
                .title("test")
                .build();
    }
}
