package com.mediconnect.user.repository;

import com.mediconnect.auth.entity.User;
import com.mediconnect.user.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, UUID> {
    List<Allergy> findAllByUserOrderByRecordedAtDesc(User user);
    List<Allergy> findAllByUserIdOrderByRecordedAtDesc(UUID userId);
    Optional<Allergy> findByIdAndUser(UUID id, User user);
}
