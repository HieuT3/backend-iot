package com.backend.system.controller;

import com.backend.system.dto.request.HistoryRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.HistoryResponse;
import com.backend.system.service.HistoryService;
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
@RequestMapping("/api/history")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class HistoryController {
    HistoryService historyService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<HistoryResponse>>> getAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "limit", defaultValue = "20") int limit,
        @RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        log.info("Fetching all histories");
        Page<HistoryResponse> historyResponses = historyService.getAll(page, limit, start, end);
        return ResponseEntity.ok(ApiResponse.<Page<HistoryResponse>>builder()
                .success(true)
                .message("Fetching all histories successfully")
                .data(historyResponses)
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<HistoryResponse>> addHistory(
            @Valid @RequestBody HistoryRequest historyRequest
    ) {
        log.info("Adding new history");
        return ResponseEntity.ok(ApiResponse.<HistoryResponse>builder()
                .success(true)
                .message("History added successfully")
                .data(historyService.addHistory(historyRequest))
                .build());
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<ApiResponse<HistoryResponse>> getHistoryById(
            @PathVariable("historyId") Long historyId
    ) {
        log.info("Fetching history with ID: {}", historyId);
        return ResponseEntity.ok(ApiResponse.<HistoryResponse>builder()
                .success(true)
                .message("History fetched successfully")
                .data(historyService.getHistoryById(historyId))
                .build());
    }

    @PutMapping("/{historyId}")
    public ResponseEntity<ApiResponse<HistoryResponse>> updateHistoryById(
            @PathVariable("historyId") Long historyId,
            @Valid @RequestBody HistoryRequest historyRequest
    ) {
        log.info("Updating history with ID: {}", historyId);
        return ResponseEntity.ok(ApiResponse.<HistoryResponse>builder()
                .success(true)
                .message("History updated successfully")
                .data(historyService.updateHistoryById(historyId, historyRequest))
                .build());
    }

    @DeleteMapping("/{historyId}")
    public ResponseEntity<ApiResponse<Void>> deleteHistoryById(
            @PathVariable("historyId") Long historyId
    ) {
        log.info("Deleting history with ID: {}", historyId);
        historyService.deleteHistoryById(historyId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("History deleted successfully")
                .build()
        );
    }
}
