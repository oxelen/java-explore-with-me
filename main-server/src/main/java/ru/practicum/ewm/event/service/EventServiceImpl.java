package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public EventFullDto create(Long userId, NewEventDto newEvent) {
        Event event = initEvent(userId, newEvent);


        return EventMapper.toFullEventDto(eventRepository.save(event));
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
}
