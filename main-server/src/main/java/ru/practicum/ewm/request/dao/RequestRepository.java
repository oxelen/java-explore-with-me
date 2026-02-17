package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select count(r) " +
            "from Request r " +
            "where r.event.id = :eventId " +
            "and r.status = 'CONFIRMED'")
    int confirmedCount(Long eventId);

    @Query("select r " +
            "from Request r " +
            "where r.event.id = :eventId")
    List<Request> findAllByEventId(Long eventId);

    @Query("select r " +
            "from Request r " +
            "where r.event.id = :eventId and r.id in :ids " +
            "order by r.created asc")
    List<Request> findRequestsByIds(Long eventId, List<Long> ids);

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByRequester(User requester);
}
