package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryRequest newCategoryRequest);

    CategoryDto update(Long catId, NewCategoryRequest newCategoryRequest);

    void delete(Long catId);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long catId);
}
