package com.mediconnect.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicationRecommendResponse {
    private String matchedSpecialization;
    private String triageUrgency;
    private List<MedicationItem> medications;
    private List<String> foodsToEat;
    private List<String> foodsToAvoid;
    private List<ScheduleSlot> schedule;
    private List<HospitalPin> nearbyHospitals;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MedicationItem {
        private String name;
        private String dosage;
        private String frequency;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ScheduleSlot {
        private String time;
        private String note;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class HospitalPin {
        private String id;
        private String name;
        private String specialization;
        private String address;
        private String phone;
        private double lat;
        private double lng;
    }
}
