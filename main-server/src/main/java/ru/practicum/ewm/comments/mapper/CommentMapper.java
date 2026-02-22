package ru.practicum.ewm.comments.mapper;

import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserDtoMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class CommentMapper {
    public static Comment toComment(NewCommentDto commentDto, User user, Event event) {
        Comment res =  new Comment();
        res.setText(commentDto.getText());
        res.setCommentator(user);
        res.setEvent(event);
        res.setCreated(LocalDateTime.now());

        return res;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .commentator(UserDtoMapper.toUserShortDto(comment.getCommentator()))
                .eventId(comment.getEvent().getId())
                .build();
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).toList();
    }
}
