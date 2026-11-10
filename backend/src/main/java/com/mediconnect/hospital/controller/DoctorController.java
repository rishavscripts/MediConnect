package com.mediconnect.hospital.controller;

import com.mediconnect.common.response.ApiResponse;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorRequest;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Public / PATIENT / ADMIN: Filter and search doctors
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DoctorResponse>>> searchDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) UUID hospitalId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<DoctorResponse> result = doctorService.searchDoctors(specialization, hospitalId, name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully", result));
    }

    // Public / PATIENT / ADMIN: Get doctor details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable UUID id) {
        DoctorResponse doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor details retrieved successfully", doctor));
    }

    // ADMIN only: Create doctor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody DoctorRequest request) {
        DoctorResponse created = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor created successfully", created));
    }

    // ADMIN only: Update doctor
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse updated = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", updated));
    }

    // ADMIN only: Delete doctor
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully", null));
    }
}
