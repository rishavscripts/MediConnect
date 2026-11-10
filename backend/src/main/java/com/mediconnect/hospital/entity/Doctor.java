package com.mediconnect.hospital.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "specialization", nullable = false, length = 120)
    private String specialization;

    @Column(name = "contact_info", length = 120)
    private String contactInfo;

    @Column(name = "source_reference", columnDefinition = "TEXT")
    private String sourceReference;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    public Doctor() {}

    public Doctor(UUID id, Hospital hospital, String name, String specialization,
                  String contactInfo, String sourceReference, OffsetDateTime verifiedAt) {
        this.id = id;
        this.hospital = hospital;
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.sourceReference = sourceReference;
        this.verifiedAt = verifiedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public OffsetDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
