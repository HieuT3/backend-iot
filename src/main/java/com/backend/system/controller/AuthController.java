package com.backend.system.controller;

import com.backend.system.dto.request.AuthRequest;
import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.TokenResponse;
import com.backend.system.dto.response.UserResponse;
import com.backend.system.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Login request for user: {}", authRequest.getUsername());
        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Login successfully")
                        .data(authService.login(authRequest))
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Register request for user: {}", authRequest.getUsername());
        authService.register(authRequest);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Registration successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Fetching current user information");
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Fetching current user information successfully")
                        .data(authService.getMe(userDetails))
                        .build()
        );
    }

    @GetMapping("/me/token")
    public ResponseEntity<ApiResponse<String>> getToken(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Fetching current user token");
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Fetching current user token successfully")
                        .data(authService.getRegistrationToken(userDetails))
                        .build()
        );
    }

    @PutMapping("/me/token")
    public ResponseEntity<ApiResponse<Void>> updateToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody RegistrationTokenRequest registrationTokenRequest) {
        log.info("Updating current user token");
        authService.updateRegistrationToken(userDetails, registrationTokenRequest);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Updating current user token successfully")
                        .data(null)
                        .build()
        );
    }
}
