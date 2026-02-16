package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.util.FindAllPublicParams;
import ru.practicum.ewm.event.util.FindAllRequestParams;
import ru.practicum.ewm.event.util.SortFilters;
import ru.practicum.ewm.event.util.UpdateEventCommonFields;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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

        return makeShortDtoList(found);
    }

    @Override
    public EventFullDto findById(Long userId, Long eventId) {
        findUserById(userId);
        Event found = getEvent(eventId);
        checkUserIsInitiator(userId, found);

        return EventMapper.toFullEventDto(found, getConfirmedCount(eventId), getEventViews(eventId));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updEventDto) {
        findUserById(userId);
        Event old = getEvent(eventId);
        checkUserIsInitiator(userId, old);

        if (old.getState().equals(State.PUBLISHED)) {
            log.warn("even must not be published");
            throw new ConstraintDeclarationException("Even must not be published");
        }

        UpdateEventCommonFields upd = UpdateEventCommonFields.builder()
                .annotation(updEventDto.getAnnotation())
                .category(updEventDto.getCategory())
                .description(updEventDto.getDescription())
                .eventDate(updEventDto.getEventDate())
                .location(updEventDto.getLocation())
                .paid(updEventDto.getPaid())
                .participantLimit(updEventDto.getParticipantLimit())
                .requestModeration(updEventDto.getRequestModeration())
                .title(updEventDto.getTitle())
                .build();

        Event filled = fillUpdEvent(old, upd);

        if (updEventDto.getStateAction() != null) {
            filled.setState(
                    switch (updEventDto.getStateAction()) {
                        case StateActionUser.SEND_TO_REVIEW -> State.PENDING;
                        case StateActionUser.CANCEL_REVIEW -> State.CANCELED;
                    }
            );
        }

        Event saved = eventRepository.save(filled);

        return EventMapper.toFullEventDto(
                saved,
                getConfirmedCount(saved.getId()),
                getEventViews(saved.getId())
        );
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updEventDto) {
        Event old = getEvent(eventId);

        UpdateEventCommonFields upd = UpdateEventCommonFields.builder()
                .annotation(updEventDto.getAnnotation())
                .category(updEventDto.getCategory())
                .description(updEventDto.getDescription())
                .eventDate(updEventDto.getEventDate())
                .location(updEventDto.getLocation())
                .paid(updEventDto.getPaid())
                .participantLimit(updEventDto.getParticipantLimit())
                .requestModeration(updEventDto.getRequestModeration())
                .title(updEventDto.getTitle())
                .build();

        Event filled = fillUpdEvent(old, upd);

        filled.setState(
                switch (updEventDto.getStateAction()) {
                    case StateActionAdmin.PUBLISH_EVENT -> {
                        if (filled.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
                            log.warn("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
                            throw new ConditionsNotMetException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
                        }

                        if (!filled.getState().equals(State.PUBLISHED)) {
                            log.warn("Cannot publish the event because it's not in the right state: PUBLISHED");
                            throw new ConditionsNotMetException("Cannot publish the event because it's not in the right state: PUBLISHED");
                        }

                        filled.setPublishedOn(LocalDateTime.now());
                        yield State.PENDING;
                    }

                    case StateActionAdmin.REJECT_EVENT -> {
                        if (filled.getState().equals(State.PUBLISHED)) {
                            log.warn("Cannot publish the event because it's not in the right state: PENDING");
                            throw new ConditionsNotMetException("Cannot publish the event because it's not in the right state: PENDING");
                        }

                        yield State.CANCELED;
                    }
                }
        );

        Event saved = eventRepository.save(filled);

        return EventMapper.toFullEventDto(
                saved,
                getConfirmedCount(saved.getId()),
                getEventViews(saved.getId())
        );
    }

    @Override
    public List<ParticipationRequestDto> getParticipantRequests(Long userId, Long eventId) {
        findUserById(userId);
        Event event = getEvent(eventId);
        checkUserIsInitiator(userId, event);

        List<Request> found = findRequestsByEventId(eventId);

        return RequestMapper.toParticipantRequestDto(found);
    }

    @Override
    public EventRequestStatusUpdateResult patchRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requests) {
        findUserById(userId);
        Event event = getEvent(eventId);
        checkUserIsInitiator(userId, event);

        if (event.getPartLimit() == getConfirmedCount(eventId)) {
            log.warn("The participant limit has been reached");
            throw new ConditionsNotMetException("The participant limit has been reached");
        }

        Queue<Request> found = requestRepository.findRequestsByIds(eventId, requests.getRequestIds());
        found.forEach(req -> {
            if (!req.getStatus().equals(Status.PENDING)) {
                log.warn("Request must have status PENDING, requestId={}", req.getId());
                throw new ValidationException("Request must have status PENDING");
            }
        });

        while (found.peek() != null) {
            Request req = found.poll();

            if (requests.getStatus().equals(Status.CONFIRMED) && event.getPartLimit() == getConfirmedCount(eventId)) {
                log.debug("The participant limit has been reached, remaining requests will be rejected");

                found.forEach(rejectReq -> {
                    rejectReq.setStatus(Status.REJECTED);
                    requestRepository.save(rejectReq);
                });
                break;
            }

            req.setStatus(requests.getStatus());
            requestRepository.save(req);
        }

        return getEventRequestStatusUpdateResult(eventId);
    }

    @Override
    public List<EventFullDto> findAllAdmin(FindAllRequestParams params) {
        if (params.getRangeEnd() != null && params.getRangeStart() != null
                && params.getRangeStart().isAfter(params.getRangeEnd())) {
            log.warn("range start cant be after range end");
            throw new ValidationException("range start cant be after range end");
        }

        BooleanExpression byUsers = params.getUsers() == null
                ? Expressions.TRUE
                : QEvent.event.initiator.id.in(params.getUsers());

        BooleanExpression byStates = params.getStates() == null
                ? Expressions.TRUE
                : QEvent.event.state.in(params.getStates());

        BooleanExpression byCategories = params.getCategories() == null
                ? Expressions.TRUE
                : QEvent.event.category.id.in(params.getCategories());

        BooleanExpression byRangeStart = params.getRangeStart() == null
                ? Expressions.TRUE
                : QEvent.event.eventDate.after(params.getRangeStart());

        BooleanExpression byRangeEnd = params.getRangeEnd() == null
                ? Expressions.TRUE
                : QEvent.event.eventDate.before(params.getRangeEnd());

        Page<Event> found = eventRepository.findAll(
                byUsers.and(byStates).and(byCategories).and(byRangeEnd).and(byRangeStart),
                PageRequest.of(0, params.getFrom() + params.getSize(), Sort.by(Sort.Direction.ASC, "eventDate"))
        );

        return makeFullDtoList(
                found.getContent().stream()
                        .skip(params.getFrom())
                        .limit(params.getSize())
                        .toList()
        );
    }

    @Override
    public List<EventShortDto> findAllPublic(FindAllPublicParams params, HttpServletRequest request) {
        BooleanExpression predicate = getPredicateForFindAllPublic(params);

        Sort sort = params.getSort() == null || params.getSort().equals(SortFilters.EVENT_DATE)
                ? Sort.by(Sort.Direction.DESC, "eventDate")
                : Sort.by(Sort.Direction.DESC, "views");

        Iterable<Event> found = eventRepository.findAll(predicate);

        List<EventShortDto> result = new ArrayList<>();

        for (Event event : found) {
            int confirmedRequests = getConfirmedCount(event.getId());
            if (!(params.getOnlyAvailable() && event.getPartLimit() > confirmedRequests)) {
                continue;
            }
            result.add(EventMapper.toEventShortDto(event, confirmedRequests, getEventViews(event.getId())));
        }

        result = result.stream()
                .sorted((event1, event2) -> {
                    if (params.getSort() == null || params.getSort().equals(SortFilters.EVENT_DATE)) {
                        return event2.getEventDate().compareTo(event1.getEventDate());
                    }

                    return event1.getViews() - event2.getViews();
                })
                .skip(params.getFrom())
                .limit(params.getSize())
                .toList();

        result.forEach((event) -> statsClient.hit(
                        CreateHitDto.builder()
                                .uri(request.getRequestURI() + "/" + event.getId())
                                .ip(request.getRemoteAddr())
                                .build()
                )
        );

        return result;
    }

    @Override
    public EventFullDto findByIdPublic(Long id, HttpServletRequest request) {
        Event event = getEvent(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("event id={} not published", id);
        }

        statsClient.hit(CreateHitDto.builder().uri(request.getRequestURI()).ip(request.getRemoteAddr()).build());

        return EventMapper.toFullEventDto(event, getConfirmedCount(id), getEventViews(id));
    }

    private BooleanExpression getPredicateForFindAllPublic(FindAllPublicParams params) {
        BooleanExpression byPublished = QEvent.event.state.eq(State.PUBLISHED);
        BooleanExpression byText = getByText(params.getText());

        BooleanExpression byCategories = params.getCategories() == null
                ? Expressions.TRUE
                : QEvent.event.category.id.in(params.getCategories());

        BooleanExpression byPaid = params.getPaid() == null
                ? Expressions.TRUE
                : QEvent.event.paid.eq(params.getPaid());

        BooleanExpression byRange = getByRange(params.getRangeStart(), params.getRangeEnd());

        return byPublished.and(byText).and(byCategories).and(byPaid).and(byRange);
    }

    private BooleanExpression getByRange(LocalDateTime start, LocalDateTime end) {
        if (start == null && end == null) {
            return QEvent.event.eventDate.after(LocalDateTime.now());
        }
        BooleanExpression byStart = start == null
                ? Expressions.TRUE
                : QEvent.event.eventDate.after(start);

        BooleanExpression byEnd = end == null
                ? Expressions.TRUE
                : QEvent.event.eventDate.before(end);

        return byStart.and(byEnd);
    }

    private BooleanExpression getByText(String text) {
        if (text == null) {
            return Expressions.TRUE;
        }
        BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(text);
        BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(text);

        return byAnnotation.or(byDescription);
    }

    private EventRequestStatusUpdateResult getEventRequestStatusUpdateResult(Long eventId) {
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        findRequestsByEventId(eventId).forEach(req -> {
            switch (req.getStatus()) {
                case Status.CONFIRMED:
                    confirmed.add(RequestMapper.toParticipantRequestDto(req));
                    break;
                case Status.REJECTED:
                    rejected.add(RequestMapper.toParticipantRequestDto(req));
            }
        });

        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    private List<Request> findRequestsByEventId(Long eventId) {
        return requestRepository.findAllByEventId(eventId);
    }

    private void checkUserIsInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("user id={} is not initiator of event id={}", userId, event.getId());
            throw new ConditionsNotMetException(
                    String.format("user id=%d is not initiator of event id=%d", userId, event.getId())
            );
        }
    }

    private int getConfirmedCount(Long eventId) {
        return requestRepository.confirmedCount(eventId);
    }

    private Event fillUpdEvent(Event old, UpdateEventCommonFields upd) {
        if (upd.getAnnotation() != null) {
            old.setAnnotation(upd.getAnnotation());
        }
        if (upd.getCategory() != null) {
            old.setCategory(findCategoryById(upd.getCategory().longValue()));
        }
        if (upd.getDescription() != null) {
            old.setDescription(upd.getDescription());
        }

        LocalDateTime eventDate = upd.getEventDate();
        if (eventDate != null) {
            checkEventDate(eventDate);
            old.setEventDate(eventDate);
        }
        if (upd.getLocation() != null) {
            old.setLocLat(upd.getLocation().lat());
            old.setLocLon(upd.getLocation().lon());
        }
        if (upd.getPaid() != null) {
            old.setPaid(upd.getPaid());
        }
        if (upd.getParticipantLimit() != null) {
            old.setPartLimit(upd.getParticipantLimit());
        }
        if (upd.getRequestModeration() != null) {
            old.setRequestModeration(upd.getRequestModeration());
        }
        if (upd.getTitle() != null) {
            old.setTitle(upd.getTitle());
        }

        return old;
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

    private List<EventShortDto> makeShortDtoList(List<Event> events) {
        return events.stream()
                .map(event ->
                        EventMapper.toEventShortDto(event,
                                requestRepository.confirmedCount(event.getId()),
                                getEventViews(event.getId()))
                )
                .toList();
    }

    private List<EventFullDto> makeFullDtoList(List<Event> events) {
        return events.stream()
                .map(event ->
                        EventMapper.toFullEventDto(event,
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
