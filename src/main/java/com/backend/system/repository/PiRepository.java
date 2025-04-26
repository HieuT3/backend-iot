package com.backend.system.repository;

import com.backend.system.entity.Pi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PiRepository extends JpaRepository<Pi, Long> {
}
