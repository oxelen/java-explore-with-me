package ru.practicum.ewm.util;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Component
public class EntityGetter {
    @Autowired
    private EntityManager em;

    public User insertUser() {

        User user = new User();
        user.setEmail("test");
        user.setName("test");

        em.persist(user);
        return user;
    }

    public Category insertCategory() {
        Category cat = new Category();
        cat.setName("test");

        em.persist(cat);
        return cat;
    }

    public Event insertEvent(User user, Category category) {
        Event event = new Event();
        event.setAnnotation("test");
        event.setCategory(category);
        event.setEventDate(LocalDateTime.MIN);
        event.setInitiator(user);
        event.setLocLat(1.1f);
        event.setLocLon(1.1f);
        event.setPaid(true);
        event.setTitle("test");

        em.persist(event);
        return event;
    }
}
