package com.backend.system.service;

import com.backend.system.dto.request.AuthRequest;
import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.response.TokenResponse;
import com.backend.system.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    TokenResponse login(AuthRequest authRequest);
    void register(AuthRequest authRequest);
    UserResponse getMe(UserDetails userDetails);
    String getRegistrationToken(UserDetails userDetails);
    void updateRegistrationToken(UserDetails userDetails, RegistrationTokenRequest registrationTokenRequest);
}
