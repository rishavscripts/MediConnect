package com.mediconnect.backend.controller;

import com.mediconnect.backend.dto.MedicationRecommendResponse;
import com.mediconnect.backend.service.OverpassHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static tools.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HospitalController {
    private final OverpassHospitalService overpassHospitalService;
    public record HealthStatus(String status, String service) {}

    @GetMapping("/health")
    public HealthStatus health() {
        return new HealthStatus("UP", "MediConnect Spring Boot 3.4");
    }

    @GetMapping("/hospitals/nearby")
    public List<MedicationRecommendResponse.HospitalPin> getNearbyHospitals(
            @RequestParam(defaultValue = "22.6942") double lat,
            @RequestParam(defaultValue = "88.3745") double lng,
            @RequestParam(required = false) String specialization
    ) {
        return overpassHospitalService.findNearbyHospitals(lat, lng, specialization);
    }
}
