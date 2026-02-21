package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository reqRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (isRequestExists(user, event)) {
            log.warn("request already exists");
            throw new ConditionsNotMetException("Request already exists");
        }

        if (event.getInitiator().equals(user)) {
            log.warn("initiator can not create request to his own event");
            throw new ConditionsNotMetException("initiator can not create request to his own event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("event must be PUBLISHED");
            throw new ConditionsNotMetException("event must be PUBLISHED");
        }

        Request req = RequestMapper.toRequest(user, event);
        if (event.getPartLimit() != 0 && event.getPartLimit() <= getConfirmedCount(eventId)) {
            log.debug("The participant limit has been reached");
            throw new ConditionsNotMetException("The participant limit has been reached");
        }
        if (!event.isRequestModeration() || event.getPartLimit() == 0) {
            log.debug("event requestModeration is false, change request status to CONFIRMED");
            req.setStatus(Status.CONFIRMED);
        }

        return RequestMapper.toParticipantRequestDto(reqRepository.save(req));
    }

    @Override
    public List<ParticipationRequestDto> findAll(Long userId) {
        User user = findUserById(userId);

        return RequestMapper.toParticipantRequestDto(reqRepository.findAllByRequester(user));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User requester = findUserById(userId);
        Request request = findRequestById(requestId);

        if (!request.getRequester().equals(requester)) {
            log.warn("User with id={} not requester of request with id={}", userId, requestId);
            throw new ConditionsNotMetException("User with id = " + userId + " not requester of request with id = " + requestId);
        }

        request.setStatus(Status.CANCELED);
        return RequestMapper.toParticipantRequestDto(request);
    }

    private Request findRequestById(Long id) {
        return reqRepository.findById(id).orElseThrow(() -> {
            log.warn("request with id = {} not found", id);
            return new NotFoundException("Request with id = " + id + " not found");
        });
    }

    private int getConfirmedCount(Long eventId) {
        return reqRepository.confirmedCount(eventId);
    }

    private boolean isRequestExists(User user, Event event) {
        return !reqRepository.findAllByEvent(event).stream()
                .filter(req -> req.getRequester().equals(user))
                .toList()
                .isEmpty();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id {} not found", userId);
            return new NotFoundException("User with id=" + userId + " was not found");
        });
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> {
            log.warn("event not found, id={}", id);
            return new NotFoundException("Event with id=" + id + " was not found");
        });
    }
}
