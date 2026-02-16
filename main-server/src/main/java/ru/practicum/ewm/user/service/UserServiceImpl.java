package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserDtoMapper;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {
        User newUser = UserDtoMapper.toUser(newUserRequest);
        return UserDtoMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public List<UserDto> findUsers(Long[] ids, int from, int size) {
        List<User> found = ids == null ?
                userRepository.findUsers(from, size)
                : userRepository.findUsers(ids, from, size);

        return UserDtoMapper.toUserDto(found);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("User with id {} not found", id);
            throw new NotFoundException("User with id=" + id + " was not found");
        }
            log.trace("User with id = {} exists", id);
            userRepository.deleteById(id);
    }
}
