package ru.practicum.ewm.comments.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.service.CommentService;
import ru.practicum.ewm.comments.util.SortType;

import java.util.List;

@RestController
@RequestMapping("/comments/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAll(@Positive @PathVariable Long eventId,
                                                    @RequestParam(defaultValue = "DESC") SortType sort) {
        log.info("GET all comments for event {}", eventId);
        List<CommentDto> res = commentService.findAllByEventId(eventId, sort);
        log.info("Comments found");

        return ResponseEntity.ok(res);
    }
}
