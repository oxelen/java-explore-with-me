package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;

public interface CategoryService {
    CategoryDto create(NewCategoryRequest newCategoryRequest);

    CategoryDto update(Long catId, NewCategoryRequest newCategoryRequest);
}
