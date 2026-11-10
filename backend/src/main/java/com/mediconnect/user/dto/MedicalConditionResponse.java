package com.mediconnect.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MedicalConditionResponse {
    private UUID id;
    private String conditionName;
    private String notes;
    private OffsetDateTime recordedAt;

    public MedicalConditionResponse() {}

    public MedicalConditionResponse(UUID id, String conditionName, String notes, OffsetDateTime recordedAt) {
        this.id = id;
        this.conditionName = conditionName;
        this.notes = notes;
        this.recordedAt = recordedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getConditionName() { return conditionName; }
    public void setConditionName(String conditionName) { this.conditionName = conditionName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public OffsetDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(OffsetDateTime recordedAt) { this.recordedAt = recordedAt; }
}
