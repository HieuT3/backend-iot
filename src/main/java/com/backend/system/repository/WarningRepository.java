package com.backend.system.repository;

import com.backend.system.entity.Warning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WarningRepository extends JpaRepository<Warning, Long> {
    Page<Warning> findAllByTimestampAfterAndTimestampBefore(LocalDateTime timestampAfter, LocalDateTime timestampBefore, Pageable pageable);
}
