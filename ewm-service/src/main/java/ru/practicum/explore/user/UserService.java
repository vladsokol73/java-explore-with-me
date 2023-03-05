package ru.practicum.explore.user;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    UserDto addUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(ArrayList<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}