package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select count(r) " +
            "from Request r " +
            "where r.event.id = :eventId " +
            "and r.status = 'ACCEPTED'")
    int confirmedCount(Long eventId);
}
