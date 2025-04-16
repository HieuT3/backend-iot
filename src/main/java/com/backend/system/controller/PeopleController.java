package com.backend.system.controller;

import com.backend.system.dto.request.PeopleRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.PeopleResponse;
import com.backend.system.entity.People;
import com.backend.system.service.PeopleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/people")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PeopleController {
    PeopleService peopleService;

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
