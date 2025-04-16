package com.backend.system.controller;

import com.backend.system.dto.request.WarningRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.entity.Warning;
import com.backend.system.service.WarningService;
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
public class WarningController {

    WarningService warningService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<Warning>>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit,
            @RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
            ) {
        log.info("Fetching all warnings");
        return ResponseEntity.ok(
                ApiResponse.<Page<Warning>>builder()
                        .success(true)
                        .message("Fetched all warnings successfully")
                        .data(warningService.getAll(page, limit, start, end))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Warning>> addWarning(
            @Valid @RequestBody WarningRequest warningRequest
            ) {
        log.info("Adding new warning");
        return ResponseEntity.ok(
                ApiResponse.<Warning>builder()
                        .success(true)
                        .message("Warning added successfully")
                        .data(warningService.addWarning(warningRequest))
                        .build()
        );
    }

    @GetMapping("/{warningId}")
    public ResponseEntity<ApiResponse<Warning>> getWarningById(
            @PathVariable("warningId") Long warningId
    ) {
        log.info("Fetching warning with ID: {}", warningId);
        return ResponseEntity.ok(
                ApiResponse.<Warning>builder()
                        .success(true)
                        .message("Warning fetched successfully")
                        .data(warningService.getWarningById(warningId))
                        .build()
        );
    }

    @PutMapping("/{warningId}")
    public ResponseEntity<ApiResponse<Warning>> updateWarningById(
            @PathVariable("warningId") Long warningId,
            @Valid @RequestBody WarningRequest warningRequest
    ) {
        log.info("Updating warning with ID: {}", warningId);
        return ResponseEntity.ok(
                ApiResponse.<Warning>builder()
                        .success(true)
                        .message("Warning updated successfully")
                        .data(warningService.updateWarningById(warningId, warningRequest))
                        .build()
        );
    }

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
