package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.mapper.CategoryDtoMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.mapper.UserDtoMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

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

    public static EventFullDto toFullEventDto(Event event) {
        return toFullEventDto(event, 0, 0);
    }

    public static EventFullDto toFullEventDto(Event event, int confirmedRequests, int views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserDtoMapper.toUserShortDto(event.getInitiator()))
                .location(new Location(event.getLocLat(), event.getLocLon()))
                .paid(event.isPaid())
                .participantLimit(event.getPartLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, int confirmedRequests, int views) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserDtoMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
