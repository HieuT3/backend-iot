package com.backend.system.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    PEOPLE_NOT_FOUND(404, "People not found", HttpStatus.NOT_FOUND),
    PEOPLE_ALREADY_EXISTS(409, "People already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXISTS(409, "Username already exists", HttpStatus.CONFLICT),
    HISTORY_NOT_FOUND(404, "History not found", HttpStatus.NOT_FOUND),
    WARNING_NOT_FOUND(404, "Warning not found", HttpStatus.NOT_FOUND),
    AUTHENTICATION_FAILED(401, "Authentication failed", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden - You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(401, "Unauthorized - You need to log in to access this resource", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(401, "The provided token is invalid", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(401, "The token has expired", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(401, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_UPLOAD_ERROR(500, "Error uploading image", HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
