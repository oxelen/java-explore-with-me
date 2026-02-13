package ru.practicum.ewm.compilation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c " +
            "from Compilation c " +
            "where c.pinned = :pinned " +
            "order by c.id " +
            "limit :size offset :from")
    List<Compilation> findAllPinned(Boolean pinned, int from, int size);

    @Query("select c " +
            "from Compilation c " +
            "order by c.id " +
            "limit :size offset :from")
    List<Compilation> findAll(int from, int size);
}
