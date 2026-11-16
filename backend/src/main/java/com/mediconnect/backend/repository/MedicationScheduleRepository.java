package com.mediconnect.backend.repository;

import com.mediconnect.backend.entity.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, UUID> {
    List<MedicationSchedule> findByUserIdAndIsActiveTrueOrderByCreatedAtAsc(UUID userId);
    Optional<MedicationSchedule> findByIdAndUserId(UUID id, UUID userId);
}
