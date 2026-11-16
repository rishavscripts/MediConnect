package com.mediconnect.backend.controller;


import com.mediconnect.backend.dto.ProfileUpdateRequest;
import com.mediconnect.backend.entity.UserProfile;
import com.mediconnect.backend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<UserProfile> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UserProfile profile = profileRepository.findById(userId)
                .orElseGet(() -> UserProfile.builder()
                        .id(userId)
                        .medicalConditions(List.of())
                        .allergies(List.of())
                        .isProfileComplete(false)
                        .build());
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfile> upsertMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ProfileUpdateRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UserProfile profile = profileRepository.findById(userId)
                .orElseGet(() -> UserProfile.builder().id(userId).build());

        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getAge() != null) profile.setAge(request.getAge());
        if (request.getGender() != null) profile.setGender(request.getGender());
        if (request.getMedicalConditions() != null) profile.setMedicalConditions(request.getMedicalConditions());
        if (request.getAllergies() != null) profile.setAllergies(request.getAllergies());
        if (request.getCustomMedicalNotes() != null) profile.setCustomMedicalNotes(request.getCustomMedicalNotes());
        if (request.getIsProfileComplete() != null) profile.setIsProfileComplete(request.getIsProfileComplete());
        profile.setUpdatedAt(OffsetDateTime.now());

        return ResponseEntity.ok(profileRepository.save(profile));
    }
}
