package ru.practicum.ewm.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST user create");
        UserDto res = userService.create(newUserRequest);
        log.info("User created, id: {}", res.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findUsers(
            @RequestParam(name = "ids", required = false) Long[] ids,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("GET users. from  {}, size = {}", from, size);
        List<UserDto> res = userService.findUsers(ids, from, size);
        log.info("Users found, size = {}", res.size());

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PositiveOrZero @PathVariable("userId") Long userId) {
        log.info("DELETE user, id: {}", userId);
        userService.deleteUser(userId);
        log.info("User deleted, id: {}", userId);
    }
}
