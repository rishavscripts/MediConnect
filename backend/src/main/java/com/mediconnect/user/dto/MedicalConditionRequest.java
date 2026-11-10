package com.mediconnect.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MedicalConditionRequest {

    @NotBlank(message = "Condition name is required")
    @Size(max = 160, message = "Condition name cannot exceed 160 characters")
    private String conditionName;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    public MedicalConditionRequest() {}

    public MedicalConditionRequest(String conditionName, String notes) {
        this.conditionName = conditionName;
        this.notes = notes;
    }

    public String getConditionName() { return conditionName; }
    public void setConditionName(String conditionName) { this.conditionName = conditionName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
