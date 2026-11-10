package com.mediconnect.hospital.controller;

import com.mediconnect.common.response.ApiResponse;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.dto.HospitalRequest;
import com.mediconnect.hospital.dto.HospitalResponse;
import com.mediconnect.hospital.service.DoctorService;
import com.mediconnect.hospital.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;
    private final DoctorService doctorService;

    public HospitalController(HospitalService hospitalService, DoctorService doctorService) {
        this.hospitalService = hospitalService;
        this.doctorService = doctorService;
    }

    // Public / PATIENT / ADMIN: Search and filter hospitals
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<HospitalResponse>>> searchHospitals(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<HospitalResponse> result = hospitalService.searchHospitals(name, location, pageable);
        return ResponseEntity.ok(ApiResponse.success("Hospitals retrieved successfully", result));
    }

    // Public / PATIENT / ADMIN: Get hospital details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalResponse>> getHospitalById(@PathVariable UUID id) {
        HospitalResponse hospital = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital details retrieved successfully", hospital));
    }

    // Public / PATIENT / ADMIN: Get doctors for a specific hospital
    @GetMapping("/{id}/doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsByHospital(@PathVariable UUID id) {
        List<DoctorResponse> doctors = doctorService.getDoctorsByHospitalId(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital doctors retrieved successfully", doctors));
    }

    // ADMIN only: Create hospital
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HospitalResponse>> createHospital(@Valid @RequestBody HospitalRequest request) {
        HospitalResponse created = hospitalService.createHospital(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hospital created successfully", created));
    }

    // ADMIN only: Update hospital
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HospitalResponse>> updateHospital(
            @PathVariable UUID id,
            @Valid @RequestBody HospitalRequest request) {
        HospitalResponse updated = hospitalService.updateHospital(id, request);
        return ResponseEntity.ok(ApiResponse.success("Hospital updated successfully", updated));
    }

    // ADMIN only: Delete hospital
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteHospital(@PathVariable UUID id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital deleted successfully", null));
    }
}
