package com.backend.system.controller;

import com.backend.system.dto.request.WarningRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.WarningResponse;
import com.backend.system.service.WarningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/warning")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Warning Controller", description = "Controller gồm các API quản lý Warning")
public class WarningController {

    WarningService warningService;

    @Operation(
            description = "API lấy danh sách tất cả warning trong hệ thống với phân trang và lọc theo thời gian."
    )
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<WarningResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit,
            @RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
            ) {
        log.info("Fetching all warnings");
        return ResponseEntity.ok(
                ApiResponse.<Page<WarningResponse>>builder()
                        .success(true)
                        .message("Fetched all warnings successfully")
                        .data(warningService.getAll(page, limit, start, end))
                        .build()
        );
    }

    @Operation(
            description = "API thêm mới warning vào hệ thống."
    )
    @PostMapping("")
    public ResponseEntity<ApiResponse<WarningResponse>> addWarning(
            @Valid @RequestBody WarningRequest warningRequest
            ) throws Exception {
        log.info("Adding new warning");
        return ResponseEntity.ok(
                ApiResponse.<WarningResponse>builder()
                        .success(true)
                        .message("Warning added successfully")
                        .data(warningService.addWarning(warningRequest))
                        .build()
        );
    }

    @Operation(
            description = "API lấy thông tin warning theo ID."
    )
    @GetMapping("/{warningId}")
    public ResponseEntity<ApiResponse<WarningResponse>> getWarningById(
            @PathVariable("warningId") Long warningId
    ) {
        log.info("Fetching warning with ID: {}", warningId);
        return ResponseEntity.ok(
                ApiResponse.<WarningResponse>builder()
                        .success(true)
                        .message("Warning fetched successfully")
                        .data(warningService.getWarningById(warningId))
                        .build()
        );
    }

    @Operation(
            description = "API cập nhật thông tin warning theo ID."
    )
    @PutMapping("/{warningId}")
    public ResponseEntity<ApiResponse<WarningResponse>> updateWarningById(
            @PathVariable("warningId") Long warningId,
            @Valid @RequestBody WarningRequest warningRequest
    ) {
        log.info("Updating warning with ID: {}", warningId);
        return ResponseEntity.ok(
                ApiResponse.<WarningResponse>builder()
                        .success(true)
                        .message("Warning updated successfully")
                        .data(warningService.updateWarningById(warningId, warningRequest))
                        .build()
        );
    }

    @Operation(
            description = "API xóa warning theo ID."
    )
    @DeleteMapping("/{warningId}")
    public ResponseEntity<ApiResponse<Void>> deleteWarningById(
            @PathVariable("warningId") Long warningId
    ) {
        log.info("Deleting warning with ID: {}", warningId);
        warningService.deleteWarningById(warningId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Warning deleted successfully")
                        .build()
        );
    }
}
