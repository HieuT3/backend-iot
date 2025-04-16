package com.backend.system.service;

import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.request.UserRequest;
import com.backend.system.dto.response.RegistrationTokenResponse;
import com.backend.system.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getUserById(Long userId);
    UserResponse addUser(UserRequest userRequest);
    UserResponse updateUserById(Long userId, UserRequest userRequest);
    void deleteUserById(Long userId);
    List<RegistrationTokenResponse> getToken();
    RegistrationTokenResponse getRegistrationTokenByUserId(Long userId);
    void updateRegistrationTokenByUserId(Long userId, RegistrationTokenRequest registrationTokenRequest);
}
