package com.mediconnect.backend.repository;

import com.mediconnect.backend.entity.DoseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoseLogRepository extends JpaRepository<DoseLog, UUID> {
    List<DoseLog> findByUserIdAndLogDate(UUID userId, LocalDate logDate);
    Optional<DoseLog> findByScheduleIdAndUserIdAndLogDate(UUID scheduleId, UUID userId, LocalDate logDate);
}
