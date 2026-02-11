package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEvent);

    List<EventShortDto> findByUser(Long userId, int from, int size);
}
