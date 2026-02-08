package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.controller.AdminCategoryController;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryRequest;
import ru.practicum.ewm.category.service.CategoryService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
public class AdminCategoryControllerTest {
    private static final String API_PREFIX = "/admin/categories";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CategoryService service;

    @Autowired
    MockMvc mvc;

    private NewCategoryRequest newDto;
    private CategoryDto responseDto;

    @BeforeEach
    public void setup() {
        newDto = NewCategoryRequest.builder()
                .name("testName")
                .build();

        responseDto = CategoryDto.builder()
                .name("testName")
                .id(1L)
                .build();
    }

    @Test
    public void createTest() throws Exception {
        when(service.create(any())).thenReturn(responseDto);

        mvc.perform(post(API_PREFIX)
                        .content(mapper.writeValueAsString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }

    @Test
    public void pathcTest() throws Exception {
        when(service.update(anyLong(), any())).thenReturn(responseDto);

        mvc.perform(patch(API_PREFIX + "/" + responseDto.getId())
                        .content(mapper.writeValueAsString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }
}
