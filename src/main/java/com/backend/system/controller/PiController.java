package com.backend.system.controller;

import com.backend.system.constant.ModeType;
import com.backend.system.dto.request.PiRequest;
import com.backend.system.dto.response.ApiResponse;
import com.backend.system.dto.response.PiResponse;
import com.backend.system.service.PiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pi")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PiController {

    PiService piService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PiResponse>>> getAll() {
        log.info("Fetching all Pi");
        return ResponseEntity.ok(
                ApiResponse.<List<PiResponse>>builder()
                        .success(true)
                        .message("Fetching all Pi successfully")
                        .data(piService.getAll())
                        .build()
        );
    }

    @GetMapping("/{piId}")
    public ResponseEntity<ApiResponse<PiResponse>> getPiById(@PathVariable("piId") Long piId) {
        log.info("Fetching Pi with ID: {}", piId);
        return ResponseEntity.ok(
                ApiResponse.<PiResponse>builder()
                        .success(true)
                        .message("Fetching Pi successfully")
                        .data(piService.getPiById(piId))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<PiResponse>> addPi(@RequestBody PiRequest piRequest) {
        log.info("Adding new Pi");
        return ResponseEntity.ok(
                ApiResponse.<PiResponse>builder()
                        .success(true)
                        .message("Pi added successfully")
                        .data(piService.addPi(piRequest))
                        .build()
        );
    }

    @PutMapping("/{piId}")
    public ResponseEntity<ApiResponse<PiResponse>> updatePiById(
            @PathVariable("piId") Long piId,
            @RequestBody PiRequest piRequest
    ) {
        log.info("Updating Pi with ID: {}", piId);
        return ResponseEntity.ok(
                ApiResponse.<PiResponse>builder()
                        .success(true)
                        .message("Pi updated successfully")
                        .data(piService.updatePiById(piId, piRequest))
                        .build()
        );
    }

    @PutMapping("/{piId}/mode")
    public ResponseEntity<ApiResponse<PiResponse>> updateMode(
            @PathVariable("piId") Long piId,
            @RequestParam("mode") ModeType mode
    ) {
        log.info("Updating Pi with ID: {} to mode: {}", piId, mode.toString());
        return ResponseEntity.ok(
                ApiResponse.<PiResponse>builder()
                        .success(true)
                        .message("Pi mode updated successfully")
                        .data(piService.updateMode(piId, mode))
                        .build()
        );
    }

    @DeleteMapping("/{piId}")
    public ResponseEntity<ApiResponse<Void>> deletePiById(@PathVariable("piId") Long piId) {
        log.info("Deleting Pi with ID: {}", piId);
        piService.deletePiById(piId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Pi deleted successfully")
                        .build()
        );
    }
}
