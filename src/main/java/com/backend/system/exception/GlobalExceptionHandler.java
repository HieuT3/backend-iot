package com.backend.system.exception;

import com.backend.system.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception e) {
//        log.error("General Exception: {}", e.getMessage());
//        return new ResponseEntity<>(
//                ApiResponse.<Void>builder()
//                        .success(false)
//                        .message("An unexpected error occurred")
//                        .data(null)
//                        .build(),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException e) {
        log.error("AppException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .data(null)
                        .build(),
                errorCode.getHttpStatusCode()
        );
    }
}
