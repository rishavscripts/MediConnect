package com.mediconnect.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProfileUpdateRequest {
    private String fullName;
    private Integer age;
    private String gender;
    private List<String> medicalConditions;
    private List<String> allergies;
    private String customMedicalNotes;
    private Boolean isProfileComplete;
}
