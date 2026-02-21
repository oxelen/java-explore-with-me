package ru.practicum.ewm.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c " +
            "from Category c " +
            "order by c.id " +
            "limit :size offset :from")
    List<Category> findAll(int from, int size);
}
