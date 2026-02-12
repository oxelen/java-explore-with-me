package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Queue;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select count(r) " +
            "from Request r " +
            "where r.event.id = :eventId " +
            "and r.status = 'ACCEPTED'")
    int confirmedCount(Long eventId);

    @Query("select r " +
            "from Request r " +
            "where r.event.id = :eventId")
    List<Request> findAllByEventId(Long eventId);

    @Query("select r " +
            "from Request r " +
            "where r.event.id = :eventId and r.id in :ids " +
            "order by r.created asc")
    Queue<Request> findRequestsByIds(Long eventId, List<Long> ids);
}
