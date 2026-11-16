package com.mediconnect.backend.service;


import com.mediconnect.backend.dto.MedicationRecommendRequest;
import com.mediconnect.backend.dto.MedicationRecommendResponse;
import com.mediconnect.backend.entity.UserProfile;
import com.mediconnect.backend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MedicationOrchestrationService {
    private final GroqAiService groqAiService;
    private final OverpassHospitalService overpassHospitalService;
    private final UserProfileRepository profileRepository;

    public MedicationRecommendResponse recommendAndLocate(MedicationRecommendRequest req, UUID authenticatedUserId) {
        // Merge stored DB medical history (if logged in) with request payload
        List<String> conditions = req.getMedicalProfile() != null ? req.getMedicalProfile().getConditions() : List.of();
        List<String> allergies = req.getMedicalProfile() != null ? req.getMedicalProfile().getAllergies() : List.of();
        String customNotes = req.getMedicalProfile() != null ? req.getMedicalProfile().getCustomNotes() : "";

        if (authenticatedUserId != null) {
            UserProfile profile = profileRepository.findById(authenticatedUserId).orElse(null);
            if (profile != null) {
                if (profile.getMedicalConditions() != null && !profile.getMedicalConditions().isEmpty()) {
                    conditions = profile.getMedicalConditions();
                }
                if (profile.getAllergies() != null && !profile.getAllergies().isEmpty()) {
                    allergies = profile.getAllergies();
                }
                if (profile.getCustomMedicalNotes() != null && !profile.getCustomMedicalNotes().isBlank()) {
                    customNotes = profile.getCustomMedicalNotes();
                }
            }
        }

        double lat = req.getLatitude() != null ? req.getLatitude() : 22.6942;
        double lng = req.getLongitude() != null ? req.getLongitude() : 88.3745;

        final List<String> finalConditions = conditions;
        final List<String> finalAllergies = allergies;
        final String finalNotes = customNotes;

        // Run Groq AI triage & Overpass API hospital lookup concurrently
        CompletableFuture<MedicationRecommendResponse> aiFuture = CompletableFuture.supplyAsync(() ->
                groqAiService.analyzeSymptoms(req.getSymptoms(), req.getSeverity(), finalConditions, finalAllergies, finalNotes)
        );

        CompletableFuture<List<MedicationRecommendResponse.HospitalPin>> mapFuture = CompletableFuture.supplyAsync(() ->
                overpassHospitalService.findNearbyHospitals(lat, lng, null)
        );

        MedicationRecommendResponse aiResult = aiFuture.join();
        List<MedicationRecommendResponse.HospitalPin> hospitals = mapFuture.join();

        // Tag returned hospitals with the AI-matched department
        String matchedDept = aiResult.getMatchedSpecialization();
        hospitals.forEach(h -> {
            if ("General Medicine".equals(h.getSpecialization()) && matchedDept != null) {
                h.setSpecialization(matchedDept);
            }
        });

        aiResult.setNearbyHospitals(hospitals);
        return aiResult;
    }
}
