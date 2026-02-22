package ru.practicum.ewm.comments.service;

import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.util.SortType;

import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto update(Long userId, Long eventId, Long commentId, NewCommentDto commentDto);

    void delete(Long commentId);

    void delete(Long userId, Long eventId, Long commentId);

    List<CommentDto> findAllByEventId(Long eventId, SortType sort);
}
