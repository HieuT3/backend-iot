package com.backend.system.service.impl;

import com.backend.system.dto.request.WarningRequest;
import com.backend.system.entity.Warning;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.repository.WarningRepository;
import com.backend.system.service.WarningService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WarningServiceImpl implements WarningService {

    WarningRepository warningRepository;

    public Page<Warning> getAll(Pageable pageable) {
        return warningRepository.findAll(pageable);
    }

    @Override
    public Page<Warning> getAll(int page, int limit, LocalDate start, LocalDate end) {
        Pageable pageable = PageRequest.of(page, limit);
        if (start == null || end == null) return getAll(pageable);
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);
        return warningRepository.findAllByTimestampAfterAndTimestampBefore(startDate, endDate, pageable);
    }

    @Override
    public Warning getWarningById(Long warningId) {
        return warningRepository.findById(warningId)
                .orElseThrow(() -> new AppException(ErrorCode.WARNING_NOT_FOUND));
    }

    @Override
    public Warning addWarning(WarningRequest warningRequest) {
        Warning warning = new Warning();
        warning.setTimestamp(warningRequest.getTimestamp());
        warning.setImagePath(warningRequest.getImagePath());
        warning.setInfo(warningRequest.getInfo());
        return warningRepository.save(warning);
    }

    @Override
    public Warning updateWarningById(Long warningId, WarningRequest warningRequest) {
        Warning existingWaring = getWarningById(warningId);
        existingWaring.setTimestamp(warningRequest.getTimestamp());
        existingWaring.setImagePath(warningRequest.getImagePath());
        existingWaring.setInfo(warningRequest.getInfo());
        return warningRepository.save(existingWaring);
    }


    @Override
    public void deleteWarningById(Long warningId) {
        Warning existingWarning = getWarningById(warningId);
        warningRepository.delete(existingWarning);
    }
}
