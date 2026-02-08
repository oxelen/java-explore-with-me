package ru.practicum.ewm.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u " +
            "from User u " +
            "order by u.id " +
            "limit :size offset :from")
    List<User> findUsers(int from, int size);

    @Query("select u " +
            "from User u " +
            "where u.id in :ids " +
            "order by u.id " +
            "limit :size offset :from")
    List<User> findUsers(Long[] ids, int from, int size);
}
