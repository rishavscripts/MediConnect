package com.mediconnect.user.repository;

import com.mediconnect.auth.entity.User;
import com.mediconnect.user.entity.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, UUID> {
    List<MedicalCondition> findAllByUserOrderByRecordedAtDesc(User user);
    List<MedicalCondition> findAllByUserIdOrderByRecordedAtDesc(UUID userId);
    Optional<MedicalCondition> findByIdAndUser(UUID id, User user);
}
