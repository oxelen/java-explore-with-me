package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final ObjectMapper mapper;

    @Override
    public EventFullDto create(Long userId, NewEventDto newEvent) {
        Event event = initEvent(userId, newEvent);

        return EventMapper.toFullEventDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> findByUser(Long userId, int from, int size) {
        findUserById(userId);
        List<Event> found = eventRepository.findByUser(userId, from, size);

        return makeUnsortedShortDtoList(found);
    }

    @Override
    public EventFullDto findById(Long userId, Long eventId) {
        findUserById(userId);
        Event found = getEvent(eventId);

        return EventMapper.toFullEventDto(found, requestRepository.confirmedCount(eventId), getEventViews(eventId));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updEvent) {
        findUserById(userId);
        Event old = getEvent(eventId);

        return null;
    }

    private Event fillUpdEvent(Event old, UpdateEventUserRequest upd) {
        if (upd.getAnnotation() != null) {
            old.setAnnotation(upd.getAnnotation());
        }
        if (upd.getCategory() != null) {
            old.setCategory(findCategoryById(upd.getCategory().longValue()));
        }
        if (upd.getDescription() != null) {
            old.setDescription(upd.getDescription());
        }
        if (upd.getEventDate() != null) {
            old.setEventDate(upd.getEventDate());
        }
        if (upd.getLocation() != null) {
            old.setLocLat(upd.getLocation().lat());
            old.setLocLon(upd.getLocation().lon());
        }
        if (upd.getPaid() != null) {
            old.setPaid(upd.getPaid());
        }
        if ()
    }

    private Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> {
            log.warn("event not found, id={}", id);
            return new NotFoundException("Event with id=" + id + " was not found");
        });
    }

    private Event initEvent(Long userId, NewEventDto dto) {
        User user = findUserById(userId);
        Category category = findCategoryById(dto.getCategory().longValue());
        checkEventDate(dto.getEventDate());

        return EventMapper.toEvent(dto, category, user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id {} not found", userId);
            return new NotFoundException("User with id=" + userId + " was not found");
        });
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Category with id {} not found", id);
            return new NotFoundException("Category with id=" + id + " was not found");
        });
    }

    private void checkEventDate(LocalDateTime eventDate) {
        LocalDateTime target = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(target)) {
            log.warn("Event date is not before 2 hours from now");
            throw new ConditionsNotMetException("должно содержать дату, которая еще не наступила.");
        }
    }

    private List<EventShortDto> makeUnsortedShortDtoList(List<Event> events) {
        return events.stream()
                .map(event ->
                        EventMapper.toEventShortDto(event,
                                requestRepository.confirmedCount(event.getId()),
                                getEventViews(event.getId()))
                )
                .toList();
    }

    private List<ResponseHitDto> findStats(String[] uris) {
        return statsClient.findStats(LocalDateTime.MIN,
                LocalDateTime.MAX,
                uris,
                false).getBody();
    }

    private int getEventViews(Long eventId) {
        String[] uris = new String[]{"/events/" + eventId};
        List<ResponseHitDto> found = findStats(uris);

        return found == null || found.isEmpty() ? 0 : found.getFirst().getHits().intValue();
    }

    /*private Map<Long, Integer> getEventsViews(List<Event> events) {
        String[] uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .toArray(String[]::new);

        Map<Long, Integer> eventViews = new HashMap<>();
        for (ResponseHitDto stat : findStats(uris)) {
            String uri = stat.getUri();
            Long id = Long.parseLong(uri.substring(uri.length() - 1));
            eventViews.put(id, stat.getHits().intValue());
        }

        return eventViews;
    }*/
}
