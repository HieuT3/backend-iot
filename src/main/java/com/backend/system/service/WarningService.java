package com.backend.system.service;

import com.backend.system.dto.request.WarningRequest;
import com.backend.system.entity.Warning;
import org.springframework.data.domain.Page;

import java.time.LocalDate;


public interface WarningService {
    Page<Warning> getAll(int page, int limit, LocalDate start, LocalDate end);
    Warning getWarningById(Long warningId);
    Warning addWarning(WarningRequest warningRequest);
    Warning updateWarningById(Long warningId, WarningRequest warningRequest);
    void deleteWarningById(Long warningId);
}
