package com.mediconnect.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiles", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    private Integer age;
    private String gender;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "medical_conditions", columnDefinition = "text[]")
    @Builder.Default
    private List<String> medicalConditions = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "allergies", columnDefinition = "text[]")
    @Builder.Default
    private List<String> allergies = new ArrayList<>();

    @Column(name = "custom_medical_notes")
    private String customMedicalNotes;

    @Column(name = "is_profile_complete")
    @Builder.Default
    private Boolean isProfileComplete = false;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
