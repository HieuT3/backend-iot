package com.backend.system.controller;

import com.backend.system.dto.request.RegistrationTokenRequest;
import com.backend.system.dto.request.UserRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.RegistrationTokenResponse;
import com.backend.system.dto.response.UserResponse;
import com.backend.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Controller", description = "Controller gồm các API quản lý User")
public class UserController {
    UserService userService;

    @Operation(
            description = "API lấy danh sách tất cả admin trong hệ thống."
    )
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        log.info("Fetching all users");
        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Fetching all users successfully")
                        .data(userService.getAll())
                        .build()
        );
    }

    @Operation(
            description = "API thêm mới admin vào hệ thống."
    )
    @PostMapping("")
    public ResponseEntity<ApiResponse<UserResponse>> addUser(
            @Valid @RequestBody UserRequest userRequest
    ) {
        log.info("Adding new  user");
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User added successfully")
                        .data(userService.addUser((userRequest)))
                        .build()
        );
    }

    @Operation(
            description = "API lấy thông tin admin theo ID."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("userId") Long userId) {
        log.info("Fetching user with ID: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User fetched successfully")
                        .data(userService.getUserById(userId))
                        .build()
        );
    }

    @Operation(
            description = "API cập nhật thông tin admin theo ID."
    )
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserById(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Update user with ID: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User updated successfully")
                        .data(userService.updateUserById(userId, userRequest))
                        .build()
        );
    }

    @Operation(
            description = "API xóa admin theo ID."
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable("userId") Long userId) {
        log.info("Delete user with ID: {}", userId);
        userService.deleteUserById(userId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User deleted successfully")
                        .build()
        );
    }

    @Operation(
            description = "API lấy danh sách tất cả registration token trong hệ thống."
    )
    @GetMapping("/token")
    public ResponseEntity<ApiResponse<List<RegistrationTokenResponse>>> getToken() {
        log.info("Fetching all tokens");
        return ResponseEntity.ok(
                ApiResponse.<List<RegistrationTokenResponse>>builder()
                        .success(true)
                        .message("Fetching all tokens successfully")
                        .data(userService.getAllRegistrationTokens())
                        .build()
        );
    }

    @Operation(
            description = "API lấy registration token của admin theo ID."
    )
    @GetMapping("/{userId}/token")
    public ResponseEntity<ApiResponse<RegistrationTokenResponse>> getTokenByUserId(@PathVariable("userId") Long userId) {
        log.info("Fetching token for user with ID: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.<RegistrationTokenResponse>builder()
                        .success(true)
                        .message("Fetching token successfully")
                        .data(userService.getRegistrationTokenByUserId(userId))
                        .build()
        );
    }

    @Operation(
            description = "API cập nhật registration token của admin theo ID."
    )
    @PutMapping("/{userId}/token")
    public ResponseEntity<ApiResponse<Void>> updateTokenByUserId(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RegistrationTokenRequest registrationTokenRequest
            ) {
        log.info("Updating token for user with ID: {}", userId);
        userService.updateRegistrationTokenByUserId(userId, registrationTokenRequest);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Token updated successfully")
                        .data(null)
                        .build()
        );
    }
}
