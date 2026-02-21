package ru.practicum.ewm.category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.error.exception.ConditionsNotMetException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.EntityGetter;
import ru.practicum.stats.client.StatsClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImplTest {
    private final EntityManager em;

    private final CategoryService categoryService;

    private final EntityGetter eg;

    @MockBean
    private StatsClient statsClient;

    @Test
    public void createTest() {
        NewCategoryRequest dto = getDefDto();
        categoryService.create(dto);

        TypedQuery<Category> query = em.createQuery("select c from Category c where c.name = :name", Category.class);
        Category res = query.setParameter("name", dto.getName()).getSingleResult();

        assertThat(res, notNullValue());
        assertThat(res.getName(), equalTo(dto.getName()));
    }

    @Test
    public void updateTest() {
        NewCategoryRequest dto = getDefDto();
        Long catId = categoryService.create(dto).getId();

        dto.setName("updName");
        categoryService.update(catId, dto);
        TypedQuery<Category> query = em.createQuery("select c from Category c where c.id = :id", Category.class);
        Category res = query.setParameter("id", catId).getSingleResult();

        assertThat(res, notNullValue());
        assertThat(res.getName(), equalTo(dto.getName()));
    }

    @Test
    public void deleteTest() {
        NewCategoryRequest dto = getDefDto();
        Long catId = categoryService.create(dto).getId();
        categoryService.delete(catId);

        TypedQuery<Category> query = em.createQuery("select c from Category c where c.id = :id", Category.class);

        Assertions.assertThrows(NoResultException.class, () -> query.setParameter("id", catId).getSingleResult());
    }

    @Test
    public void deleteCategoryExtendEventShouldThrowException() {
        Category cat = eg.insertCategory();
        User user = eg.insertUser();
        eg.insertEvent(user, cat);

        Assertions.assertThrows(ConditionsNotMetException.class, () -> categoryService.delete(cat.getId()));
    }

    private NewCategoryRequest getDefDto() {
        return NewCategoryRequest.builder().name("test").build();
    }
}
