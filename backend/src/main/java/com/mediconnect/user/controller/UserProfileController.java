package com.mediconnect.user.controller;

import com.mediconnect.common.exception.UnauthorizedException;
import com.mediconnect.common.response.ApiResponse;
import com.mediconnect.user.dto.*;
import com.mediconnect.user.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    private String getAuthenticatedUserEmail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("User is not authenticated");
        }
        return authentication.getName();
    }

    // --- Personal Profile ---

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(Authentication authentication) {
        String email = getAuthenticatedUserEmail(authentication);
        UserProfileResponse profile = userProfileService.getProfile(email);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        String email = getAuthenticatedUserEmail(authentication);
        UserProfileResponse updated = userProfileService.updateProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", updated));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> patchProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return updateProfile(authentication, request);
    }

    // --- Medical Profile Summary ---

    @GetMapping("/medical-profile")
    public ResponseEntity<ApiResponse<MedicalProfileResponse>> getMedicalProfile(Authentication authentication) {
        String email = getAuthenticatedUserEmail(authentication);
        MedicalProfileResponse medicalProfile = userProfileService.getMedicalProfile(email);
        return ResponseEntity.ok(ApiResponse.success("Medical profile retrieved successfully", medicalProfile));
    }

    // --- Medical Conditions ---

    @GetMapping("/conditions")
    public ResponseEntity<ApiResponse<List<MedicalConditionResponse>>> getConditions(Authentication authentication) {
        String email = getAuthenticatedUserEmail(authentication);
        List<MedicalConditionResponse> conditions = userProfileService.getConditions(email);
        return ResponseEntity.ok(ApiResponse.success("Medical conditions retrieved successfully", conditions));
    }

    @PostMapping("/conditions")
    public ResponseEntity<ApiResponse<MedicalConditionResponse>> addCondition(
            Authentication authentication,
            @Valid @RequestBody MedicalConditionRequest request) {
        String email = getAuthenticatedUserEmail(authentication);
        MedicalConditionResponse created = userProfileService.addCondition(email, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medical condition recorded successfully", created));
    }

    @PutMapping("/conditions/{id}")
    public ResponseEntity<ApiResponse<MedicalConditionResponse>> updateCondition(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody MedicalConditionRequest request) {
        String email = getAuthenticatedUserEmail(authentication);
        MedicalConditionResponse updated = userProfileService.updateCondition(email, id, request);
        return ResponseEntity.ok(ApiResponse.success("Medical condition updated successfully", updated));
    }

    @DeleteMapping("/conditions/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCondition(
            Authentication authentication,
            @PathVariable UUID id) {
        String email = getAuthenticatedUserEmail(authentication);
        userProfileService.deleteCondition(email, id);
        return ResponseEntity.ok(ApiResponse.success("Medical condition deleted successfully", null));
    }

    // --- Allergies ---

    @GetMapping("/allergies")
    public ResponseEntity<ApiResponse<List<AllergyResponse>>> getAllergies(Authentication authentication) {
        String email = getAuthenticatedUserEmail(authentication);
        List<AllergyResponse> allergies = userProfileService.getAllergies(email);
        return ResponseEntity.ok(ApiResponse.success("Allergies retrieved successfully", allergies));
    }

    @PostMapping("/allergies")
    public ResponseEntity<ApiResponse<AllergyResponse>> addAllergy(
            Authentication authentication,
            @Valid @RequestBody AllergyRequest request) {
        String email = getAuthenticatedUserEmail(authentication);
        AllergyResponse created = userProfileService.addAllergy(email, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Allergy recorded successfully", created));
    }

    @PutMapping("/allergies/{id}")
    public ResponseEntity<ApiResponse<AllergyResponse>> updateAllergy(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody AllergyRequest request) {
        String email = getAuthenticatedUserEmail(authentication);
        AllergyResponse updated = userProfileService.updateAllergy(email, id, request);
        return ResponseEntity.ok(ApiResponse.success("Allergy updated successfully", updated));
    }

    @DeleteMapping("/allergies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAllergy(
            Authentication authentication,
            @PathVariable UUID id) {
        String email = getAuthenticatedUserEmail(authentication);
        userProfileService.deleteAllergy(email, id);
        return ResponseEntity.ok(ApiResponse.success("Allergy deleted successfully", null));
    }
}
