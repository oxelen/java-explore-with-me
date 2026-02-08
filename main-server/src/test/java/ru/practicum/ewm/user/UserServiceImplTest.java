package ru.practicum.ewm.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImplTest {
    private static final int DEF_SIZE = 10;
    private static final int DEF_FROM = 0;

    private final EntityManager em;

    private final UserService userService;

    @Test
    public void createUserTest() {
        NewUserRequest reqDto = getDefaultNewUserRequest();
        userService.create(reqDto);

        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
        User res = query.setParameter("email", reqDto.getEmail()).getSingleResult();

        assertThat(res.getId(), notNullValue());
        assertThat(res.getName(), equalTo(reqDto.getName()));
        assertThat(res.getEmail(), equalTo(reqDto.getEmail()));
    }

    @Test
    public void duplicatedEmailShouldThrowException() {
        NewUserRequest reqDto = getDefaultNewUserRequest();
        userService.create(reqDto);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.create(reqDto);
        });
    }

    @Test
    public void nullNameShouldThrowException() {
        NewUserRequest reqDto = getNewUserRequest(null, "email@ex.com");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.create(reqDto);
        });
    }

    @Test
    public void nullEmailShouldThrowException() {
        NewUserRequest reqDto = getNewUserRequest("email", null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.create(reqDto);
        });
    }

    @Test
    public void findUsersTest() {
        createAndSaveManyUsers(DEF_SIZE);
        List<UserDto> found = userService.findUsers(null, DEF_FROM, DEF_SIZE);

        assertThat(found.size(), equalTo(DEF_SIZE));
    }

    @Test
    public void findUsersTestByIds() {
        createAndSaveManyUsers(DEF_SIZE);

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> found = query.getResultList();

        Long[] ids = new Long[]{
                found.getFirst().getId(),
                found.getLast().getId()
        };

        List<UserDto> res = userService.findUsers(ids, DEF_FROM, DEF_SIZE);

        assertThat(res.size(), equalTo(ids.length));

        List<Long> resIds = res.stream().map(UserDto::getId).toList();
        assertThat(resIds, containsInAnyOrder(ids));
    }

    @Test
    public void findUsersWithDefaultFromTest() {
        final int from = 2;
        createAndSaveManyUsers(from + DEF_SIZE);

        List<UserDto> res = userService.findUsers(null, from, DEF_SIZE);

        assertThat(res.size(), equalTo(DEF_SIZE));
    }

    @Test
    public void deleteTest() {
        UserDto userDto = userService.create(getDefaultNewUserRequest());
        userService.deleteUser(userDto.getId());
        TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);

        Assertions.assertThrows(NoResultException.class, () -> {
            query.setParameter("id", userDto.getId()).getSingleResult();
        });
    }

    private NewUserRequest getDefaultNewUserRequest() {
        String name = "testName";
        return NewUserRequest.builder()
                .name(name)
                .email(name + "@test.com")
                .build();
    }

    private NewUserRequest getNewUserRequest(String name, String email) {
        return NewUserRequest.builder()
                .name(name)
                .email(email)
                .build();
    }

    private void createAndSaveManyUsers(int cnt) {
        for (int i = 0; i < cnt; i++) {
            NewUserRequest reqDto = getNewUserRequest("name" + i, "email" + i + "@test.com");
            userService.create(reqDto);
        }
    }
}
