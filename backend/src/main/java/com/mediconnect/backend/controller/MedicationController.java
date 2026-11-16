package com.mediconnect.backend.controller;

import com.mediconnect.backend.dto.MedicationRecommendRequest;
import com.mediconnect.backend.dto.MedicationRecommendResponse;
import com.mediconnect.backend.service.MedicationOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationOrchestrationService orchestrationService;

    @PostMapping("/recommend")
    public ResponseEntity<MedicationRecommendResponse> recommend(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody MedicationRecommendRequest request
    ) {
        UUID userId = (jwt != null) ? UUID.fromString(jwt.getSubject()) : null;
        return ResponseEntity.ok(orchestrationService.recommendAndLocate(request, userId));
    }
}
