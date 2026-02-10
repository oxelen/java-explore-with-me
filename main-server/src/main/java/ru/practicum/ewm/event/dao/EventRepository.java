package ru.practicum.ewm.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select count(e.category) " +
            "from Event e " +
            "where e.category.id = :catId")
    int findCountByCategory(Long catId);
}
