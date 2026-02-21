package ru.practicum.ewm.category.mapper;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public class CategoryDtoMapper {
    public static Category toCategory(NewCategoryRequest dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDto(List<Category> categories) {
        return categories.stream().map(CategoryDtoMapper::toCategoryDto).toList();
    }
}
