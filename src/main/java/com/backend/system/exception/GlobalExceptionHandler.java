package com.backend.system.exception;

import com.backend.system.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e,
                                                                HttpServletRequest request) {
        log.error("General Exception: {}", e.getMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException e,
                                                            HttpServletRequest request) {
        log.error("AppException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .message(errorCode.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(),
                errorCode.getHttpStatusCode()
        );
    }

    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException e,
                                                              HttpServletRequest request) {
        log.error("TokenException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .message(errorCode.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(),
                errorCode.getHttpStatusCode()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .message("Validation failed")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .errors(errors)
                        .build(),
                e.getStatusCode()
        );
    }
}
