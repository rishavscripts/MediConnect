package com.mediconnect.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AllergyResponse {
    private UUID id;
    private String substance;
    private String reactionNotes;
    private OffsetDateTime recordedAt;

    public AllergyResponse() {}

    public AllergyResponse(UUID id, String substance, String reactionNotes, OffsetDateTime recordedAt) {
        this.id = id;
        this.substance = substance;
        this.reactionNotes = reactionNotes;
        this.recordedAt = recordedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSubstance() { return substance; }
    public void setSubstance(String substance) { this.substance = substance; }
    public String getReactionNotes() { return reactionNotes; }
    public void setReactionNotes(String reactionNotes) { this.reactionNotes = reactionNotes; }
    public OffsetDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(OffsetDateTime recordedAt) { this.recordedAt = recordedAt; }
}
