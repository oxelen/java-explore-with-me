package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.user.controller.AdminUserController;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
public class AdminUserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private NewUserRequest createDto;
    private UserDto responseDto;

    @BeforeEach
    public void setup() {
        createDto = NewUserRequest.builder()
                .name("testName")
                .email("testEmail@test.com")
                .build();

        responseDto = UserDto.builder()
                .id(1L)
                .name("testName")
                .email("testEmail@test.com")
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.create(any())).thenReturn(responseDto);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(createDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()));
    }

    @Test
    public void findUsersTest() throws Exception {
        when(userService.findUsers(any(), anyInt(), anyInt())).thenReturn(List.of(responseDto));

        mvc.perform(get("/admin/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mvc.perform(delete("/admin/users/" + responseDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().is(204));
    }
}
