package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.mapper.CategoryDtoMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepository;

    @Override
    public CategoryDto create(NewCategoryRequest newCategoryRequest) {
        Category category = CategoryDtoMapper.toCategory(newCategoryRequest);
        return CategoryDtoMapper.toCategoryDto(catRepository.save(category));
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryRequest newCategoryRequest) {
        if (catRepository.findById(catId).isEmpty()) {
            log.warn("Category with id = {} does not found", catId);
            throw new NotFoundException("Category with id=" + catId + " not found");
        }
        log.trace("Category with id = {} exists", catId);

        Category cat = CategoryDtoMapper.toCategory(newCategoryRequest);
        cat.setId(catId);

        return CategoryDtoMapper.toCategoryDto(catRepository.save(cat));
    }
}
