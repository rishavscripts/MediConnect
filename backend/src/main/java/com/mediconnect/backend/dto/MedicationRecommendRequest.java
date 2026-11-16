package com.mediconnect.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MedicationRecommendRequest {
    private String symptoms;
    private String severity;
    private Double latitude;
    private Double longitude;
    private MedicalProfileSnapshot medicalProfile;

    @Data
    public static class MedicalProfileSnapshot {
        private List<String> conditions;
        private List<String> allergies;
        private String customNotes;
    }
}
