package com.mediconnect.user.dto;

import java.util.List;

public class MedicalProfileResponse {
    private List<MedicalConditionResponse> conditions;
    private List<AllergyResponse> allergies;

    public MedicalProfileResponse() {}

    public MedicalProfileResponse(List<MedicalConditionResponse> conditions, List<AllergyResponse> allergies) {
        this.conditions = conditions;
        this.allergies = allergies;
    }

    public List<MedicalConditionResponse> getConditions() {
        return conditions;
    }

    public void setConditions(List<MedicalConditionResponse> conditions) {
        this.conditions = conditions;
    }

    public List<AllergyResponse> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<AllergyResponse> allergies) {
        this.allergies = allergies;
    }
}
