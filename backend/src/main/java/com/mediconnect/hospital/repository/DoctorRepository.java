package com.mediconnect.hospital.repository;

import com.mediconnect.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM Doctor d LEFT JOIN d.hospital h WHERE " +
           "(:specialization IS NULL OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))) AND " +
           "(:hospitalId IS NULL OR h.id = :hospitalId) AND " +
           "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Doctor> searchDoctors(@Param("specialization") String specialization,
                               @Param("hospitalId") UUID hospitalId,
                               @Param("name") String name,
                               Pageable pageable);

    List<Doctor> findByHospitalId(UUID hospitalId);
}
