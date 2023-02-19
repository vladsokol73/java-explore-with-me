package ru.practicum.explore.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.error.BadRequest;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.user.NewUserRequest;
import ru.practicum.user.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        if (Objects.isNull(newUserRequest.getEmail()) ||  Objects.isNull(newUserRequest.getName())) {
            throw new BadRequest("Invalid request");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getUsers(ArrayList<Long> ids, Integer from, Integer size) {
        List<User> users = userRepository.getAllByIdIsIn(ids, PageRequest.of(from, size));
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("User with id=" + id + " was not found"));
        userRepository.delete(user);
    }
}
