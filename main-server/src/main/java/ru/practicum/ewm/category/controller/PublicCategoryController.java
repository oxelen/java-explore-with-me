package ru.practicum.ewm.category.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @PositiveOrZero @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET all categories");
        List<CategoryDto> res = categoryService.findAll(from, size);
        log.info("categories found");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> findById(@PositiveOrZero @PathVariable Long catId) {
        log.info("GET category by id={}", catId);
        CategoryDto res = categoryService.findById(catId);
        log.info("category id={} found", catId);

        return ResponseEntity.ok(res);
    }
}
