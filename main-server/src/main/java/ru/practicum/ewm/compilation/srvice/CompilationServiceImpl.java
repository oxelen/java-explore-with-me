package ru.practicum.ewm.compilation.srvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dao.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationDtoMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;

    private final StatsClient statsClient;

    @Override
    public CompilationDto create(NewCompilationDto newCompilation) {
        Set<Event> events = getEventsFromDto(newCompilation.getEvents());
        Compilation compilation = CompilationDtoMapper.toCompilation(newCompilation, events);

        return CompilationDtoMapper.toCompilationDto(
                compilationRepository.save(compilation),
                makeUnsortedShortDtoList(events)
        );
    }

    @Override
    public void delete(Long compId) {
        Compilation compilation = getById(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updDto) {
        Compilation old = getById(compId);
        Compilation saved = compilationRepository.save(fillUpdCompilation(old, updDto));

        return CompilationDtoMapper.toCompilationDto(
                saved, makeUnsortedShortDtoList(getEventsFromDto(updDto.getEvents()))
        );
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        List<Compilation> found = pinned == null
                ? compilationRepository.findAll(from, size)
                : compilationRepository.findAllPinned(pinned, from, size);

        return found.stream()
                .map(comp
                        -> CompilationDtoMapper.toCompilationDto(
                                comp, makeUnsortedShortDtoList(comp.getEvents())
                ))
                .toList();
    }

    @Override
    public CompilationDto findById(Long compId) {
        Compilation res = getById(compId);
        Set<Event> events = res.getEvents();
        return CompilationDtoMapper.toCompilationDto(res, makeUnsortedShortDtoList(events));
    }

    private Compilation fillUpdCompilation(Compilation old, UpdateCompilationRequest upd) {
        if (upd.getEvents() != null) {
            old.setEvents(getEventsFromDto(upd.getEvents()));
        }
        if (upd.getPinned() != null) {
            old.setPinned(upd.getPinned());
        }
        if (upd.getTitle() != null) {
            old.setTitle(upd.getTitle());
        }

        return old;
    }

    private Set<Event> getEventsFromDto(List<Long> eventIds) {
        return eventIds.stream().map(this::findEventById).collect(Collectors.toSet());
    }

    private Compilation getById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            log.warn("Compilation with id={}} was not found", compId);
            return new NotFoundException("Compilation with id=" + compId + " was not found");
        });
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> {
            log.warn("event not found, id={}", id);
            return new NotFoundException("Event with id=" + id + " was not found");
        });
    }

    private List<EventShortDto> makeUnsortedShortDtoList(Set<Event> events) {
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
}
