package com.mediconnect.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AllergyRequest {

    @NotBlank(message = "Substance is required")
    @Size(max = 160, message = "Substance cannot exceed 160 characters")
    private String substance;

    @Size(max = 1000, message = "Reaction notes cannot exceed 1000 characters")
    private String reactionNotes;

    public AllergyRequest() {}

    public AllergyRequest(String substance, String reactionNotes) {
        this.substance = substance;
        this.reactionNotes = reactionNotes;
    }

    public String getSubstance() { return substance; }
    public void setSubstance(String substance) { this.substance = substance; }
    public String getReactionNotes() { return reactionNotes; }
    public void setReactionNotes(String reactionNotes) { this.reactionNotes = reactionNotes; }
}
