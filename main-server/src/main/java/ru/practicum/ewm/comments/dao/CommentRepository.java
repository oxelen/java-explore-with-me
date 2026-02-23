package ru.practicum.ewm.comments.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c " +
            "from Comment c " +
            "where c.event = :event " +
            "order by c.created asc")
    List<Comment> findAllAsc(Event event);

    @Query("select c " +
            "from Comment c " +
            "where c.event = :event " +
            "order by c.created desc")
    List<Comment> findAllDesc(Event event);
}
