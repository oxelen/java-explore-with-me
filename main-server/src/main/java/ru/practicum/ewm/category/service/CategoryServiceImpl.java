package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.mapper.CategoryDtoMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dao.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryRequest newCategoryRequest) {
        Category category = CategoryDtoMapper.toCategory(newCategoryRequest);
        return CategoryDtoMapper.toCategoryDto(catRepository.save(category));
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryRequest newCategoryRequest) {
        checkId(catId);

        Category cat = CategoryDtoMapper.toCategory(newCategoryRequest);
        cat.setId(catId);

        return CategoryDtoMapper.toCategoryDto(catRepository.save(cat));
    }

    @Override
    public void delete(Long catId) {
        checkId(catId);

        if (eventRepository.findCountByCategory(catId) != 0) {
            log.warn("category is not empty");
            throw new ConditionsNotMetException("Category is not empty");
        }
        log.trace("category is empty");

        catRepository.deleteById(catId);
    }

    private void checkId(Long catId) {
        if (catRepository.findById(catId).isEmpty()) {
            log.warn("Category with id = {} does not found", catId);
            throw new NotFoundException("Category with id=" + catId + " not found");
        }
        log.trace("Category with id = {} exists", catId);
    }
}
