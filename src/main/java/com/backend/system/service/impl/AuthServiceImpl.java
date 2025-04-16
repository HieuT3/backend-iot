package com.backend.system.service.impl;

import com.backend.system.dto.request.AuthRequest;
import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.response.TokenResponse;
import com.backend.system.dto.response.UserResponse;
import com.backend.system.entity.User;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.UserMapper;
import com.backend.system.repository.UserRepository;
import com.backend.system.security.CustomUserDetails;
import com.backend.system.security.JwtAuthenticationProvider;
import com.backend.system.service.AuthService;
import com.backend.system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtAuthenticationProvider jwtAuthenticationProvider;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    UserService userService;

    @Override
    public TokenResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    ));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtAuthenticationProvider.generateToken(userDetails);

            return TokenResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiration(jwtAuthenticationProvider.extractExpiration(token).getTime())
                    .build();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", authRequest.getUsername(), e);
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    @Override
    public void register(AuthRequest authRequest) {
        if (userRepository.findUserByUsername(authRequest.getUsername()).isPresent())
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse getMe(UserDetails userDetails) {
        return userMapper.toUserResponse(getUserInAuthentication(userDetails));
    }

    @Override
    public String getRegistrationToken(UserDetails userDetails) {
        User user = getUserInAuthentication(userDetails);
        return user.getRegistrationToken();
    }

    @Override
    public void updateRegistrationToken(UserDetails userDetails,
                                        RegistrationTokenRequest registrationTokenRequest) {
        User user = getUserInAuthentication(userDetails);
        userService.updateRegistrationTokenByUserId(user.getUserId(), registrationTokenRequest);
    }

    private User getUserInAuthentication(UserDetails userDetails) {
        return ((CustomUserDetails) userDetails).getUser();
    }
}
