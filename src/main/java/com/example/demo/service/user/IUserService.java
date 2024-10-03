package com.example.demo.service.user;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
