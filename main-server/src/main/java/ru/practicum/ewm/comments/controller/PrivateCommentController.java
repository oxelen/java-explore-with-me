package ru.practicum.ewm.comments.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/comment/{eventId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long eventId,
            @Valid @RequestBody NewCommentDto comment
            ) {
        log.info("POST comment, userId={}, eventId={}", userId, eventId);
        CommentDto res = commentService.create(userId, eventId, comment);
        log.info("comment created, id={}", res.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> patch(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long eventId,
            @PositiveOrZero @PathVariable Long commentId,
            @Valid @RequestBody NewCommentDto commentDto
    ) {
        log.info("PATCH comment, userId={}, eventId={}", userId, eventId);
        CommentDto res = commentService.update(userId, eventId, commentId, commentDto);
        log.info("comment updated, id={}", res.getId());

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PositiveOrZero @PathVariable Long userId,
            @PositiveOrZero @PathVariable Long eventId,
            @PositiveOrZero @PathVariable Long commentId
    ) {
        log.info("DELETE comment, userId={}, eventId={}", userId, eventId);
        commentService.delete(userId, eventId, commentId);
        log.info("comment deleted, id={}", commentId);
    }
}
