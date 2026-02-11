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
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.EntityGetter;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class EventServiceImplTest {
    private final EntityManager em;
    private final EventService eventService;
    private final EntityGetter eg;

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
