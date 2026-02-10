package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event toEvent(NewEventDto dto, Category category, User initiator) {
        Event event = new Event();

        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setInitiator(initiator);
        event.setLocLat(dto.getLocation().lat());
        event.setLocLon(dto.getLocation().lon());
        event.setPaid(dto.isPaid());
        event.setPartLimit(dto.getPartLimit());
        event.setRequestModeration(dto.isRequestModeration());
        event.setState(State.PENDING);
        event.setTitle(dto.getTitle());

        return event;
    }
}
