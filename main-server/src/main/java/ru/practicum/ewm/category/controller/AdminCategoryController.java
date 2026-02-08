package ru.practicum.ewm.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody NewCategoryRequest newCategoryRequest) {
        log.info("POST create category");
        CategoryDto res = categoryService.create(newCategoryRequest);
        log.info("Category created");

        return ResponseEntity
                .status(201)
                .body(res);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patch(@PositiveOrZero @PathVariable("catId")
                                             Long catId,
                                             @Valid @RequestBody
                                             NewCategoryRequest newCategoryRequest
                                             ) {
        log.info("Patch category");
        CategoryDto res = categoryService.update(catId, newCategoryRequest);
        log.info("Category updated");

        return ResponseEntity
                .status(200)
                .body(res);
    }
}
