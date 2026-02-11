package ru.practicum.ewm.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select count(e.category) " +
            "from Event e " +
            "where e.category.id = :catId")
    int findCountByCategory(Long catId);

    @Query("select e " +
            "from Event e " +
            "where e.initiator.id = :userId " +
            "order by e.id " +
            "limit :size offset :from")
    List<Event> findByUser(Long userId, int from, int size);
}
