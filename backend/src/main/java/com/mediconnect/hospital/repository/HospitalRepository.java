package com.mediconnect.hospital.repository;

import com.mediconnect.hospital.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, UUID> {

    @Query("SELECT h FROM Hospital h WHERE " +
           "(:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:location IS NULL OR LOWER(h.address) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Hospital> searchHospitals(@Param("name") String name,
                                   @Param("location") String location,
                                   Pageable pageable);
}
