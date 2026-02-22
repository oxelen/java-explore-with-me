package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comments.dao.CommentRepository;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.mapper.CommentMapper;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.util.SortType;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("Can not comment not published events");
            throw new ConditionsNotMetException("Can not comment not published events");
        }
        Comment comment = CommentMapper.toComment(commentDto, user, event);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(Long userId, Long eventId, Long commentId, NewCommentDto commentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        Comment old = getComment(commentId);

        checkCommentConditions(user, event, old);

        old.setText(commentDto.getText());

        return CommentMapper.toCommentDto(commentRepository.save(old));
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void delete(Long userId, Long eventId, Long commentId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        Comment comment = getComment(commentId);

        checkCommentConditions(user, event, comment);

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> findAllByEventId(Long eventId, SortType sort) {
        Event event = getEvent(eventId);

        return CommentMapper.toCommentDto(
                switch (sort) {
                    case SortType.ASC -> commentRepository.findAllAsc(event);
                    case SortType.DESC -> commentRepository.findAllDesc(event);
                }
        );
    }

    private void checkCommentConditions(User user, Event event, Comment comment) {
        if (!comment.getCommentator().equals(user)) {
            log.warn("user id={} is not commentator of comment id={}", user.getId(), comment.getId());
            throw new ConditionsNotMetException("user is not commentator of comment id=" + comment.getId());
        }

        if (!comment.getEvent().equals(event)) {
            log.warn("comment id={} is not for event id={}", comment.getId(), event.getId());
            throw new ConditionsNotMetException("comment is not for event id=" + event.getId());
        }
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> {
            log.warn("event not found, id={}", eventId);
            return new NotFoundException("Event with id=" + eventId + " was not found");
        });
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id {} not found", userId);
            return new NotFoundException("User with id=" + userId + " was not found");
        });
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.warn("comment not found, id={}", commentId);
            return new NotFoundException("Comment with id=" + commentId + " was not found");
        });
    }
}
