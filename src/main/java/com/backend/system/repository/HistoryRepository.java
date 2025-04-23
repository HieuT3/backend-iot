package com.backend.system.repository;

import com.backend.system.entity.History;
import com.backend.system.entity.People;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findAllByTimestampAfterAndTimestampBefore(LocalDateTime timestampAfter, LocalDateTime timestampBefore, Pageable pageable);

    Page<History> findAllByPeople(People people, Pageable pageable);
}
