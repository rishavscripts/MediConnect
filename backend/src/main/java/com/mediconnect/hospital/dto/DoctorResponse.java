package com.mediconnect.hospital.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DoctorResponse {
    private UUID id;
    private String name;
    private String specialization;
    private String contactInfo;
    private UUID hospitalId;
    private String hospitalName;
    private String sourceReference;
    private OffsetDateTime verifiedAt;

    public DoctorResponse() {}

    public DoctorResponse(UUID id, String name, String specialization, String contactInfo,
                          UUID hospitalId, String hospitalName,
                          String sourceReference, OffsetDateTime verifiedAt) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.sourceReference = sourceReference;
        this.verifiedAt = verifiedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(UUID hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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
