package com.backend.system.service;

import com.backend.system.dto.request.WarningRequest;
import com.backend.system.dto.response.WarningResponse;
import com.backend.system.entity.Warning;
import org.springframework.data.domain.Page;

import java.time.LocalDate;


public interface WarningService {
    Page<WarningResponse> getAll(int page, int limit, LocalDate start, LocalDate end);
    WarningResponse getWarningById(Long warningId);
    Warning getWarningEntityById(Long warningId);
    WarningResponse addWarning(WarningRequest warningRequest) throws Exception;
    WarningResponse updateWarningById(Long warningId, WarningRequest warningRequest);
    void deleteWarningById(Long warningId);
}
