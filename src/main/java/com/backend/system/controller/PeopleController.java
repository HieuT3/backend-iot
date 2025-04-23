package com.backend.system.controller;

import com.backend.system.dto.request.PeopleRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.PeopleResponse;

import com.backend.system.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/people")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "People Controller", description = " Controller gồm các API quản lý People")
public class PeopleController {
    PeopleService peopleService;

    @Operation(
            description = "API lấy danh sách tất cả người dùng trong hệ thống với phân trang."
    )
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<PeopleResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit
    ) {
        log.info("Fetching all people");
        return ResponseEntity.ok(
                ApiResponse.<Page<PeopleResponse>>builder()
                        .success(true)
                        .message("Fetching all people successfully")
                        .data(peopleService.getAll(page, limit))
                        .build()
        );
    }

    @Operation(
            description = "API thêm người dùng mới vào hệ thống."
    )
    @PostMapping("")
    public ResponseEntity<ApiResponse<PeopleResponse>> addPeople(
            @Valid @ModelAttribute PeopleRequest peopleRequest
    ) {
        log.info("Adding new people");
        return ResponseEntity.ok(
                ApiResponse.<PeopleResponse>builder()
                        .success(true)
                        .message("People added successfully")
                        .data(peopleService.addPeople(peopleRequest))
                        .build()
        );
    }

    @Operation(
            description = "API lấy thông tin người dùng theo ID."
    )
    @GetMapping("/{peopleId}")
    public ResponseEntity<ApiResponse<PeopleResponse>> getPeopleById(
            @PathVariable("peopleId") Long peopleId) {
        log.info("Fetching people with ID: {}", peopleId);
        return ResponseEntity.ok(
                ApiResponse.<PeopleResponse>builder()
                        .success(true)
                        .message("People fetched successfully")
                        .data(peopleService.getPeopleDtoById(peopleId))
                        .build()
        );
    }

    @Operation(
            description = "API cập nhật thông tin người dùng theo ID."
    )
    @PutMapping("/{peopleId}")
    public ResponseEntity<ApiResponse<PeopleResponse>> updatePeopleById(
            @PathVariable("peopleId") Long peopleId,
            @Valid @ModelAttribute PeopleRequest peopleRequest) {
        log.info("Updating people with ID: {}", peopleId);
        return ResponseEntity.ok(
                ApiResponse.<PeopleResponse>builder()
                        .success(true)
                        .message("People updated successfully")
                        .data(peopleService.updatePeopleById(peopleId, peopleRequest))
                        .build()
        );
    }

    @Operation(
            description = "API xóa người dùng theo ID."
    )
    @DeleteMapping("/{peopleId}")
    public ResponseEntity<ApiResponse<Void>> deletePeopleById(@PathVariable("peopleId") Long peopleId) {
        log.info("Deleting people with ID: {}", peopleId);
        peopleService.deletePeopleById(peopleId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("People deleted successfully")
                        .build()
        );
    }
}
