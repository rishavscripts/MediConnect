package com.mediconnect.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class DoctorRequest {

    @NotBlank(message = "Doctor name is required")
    @Size(max = 160, message = "Doctor name cannot exceed 160 characters")
    private String name;

    @NotBlank(message = "Specialization is required")
    @Size(max = 120, message = "Specialization cannot exceed 120 characters")
    private String specialization;

    @Size(max = 120, message = "Contact info cannot exceed 120 characters")
    private String contactInfo;

    private UUID hospitalId;

    @Size(max = 1000, message = "Source reference cannot exceed 1000 characters")
    private String sourceReference;

    public DoctorRequest() {}

    public DoctorRequest(String name, String specialization, String contactInfo, UUID hospitalId, String sourceReference) {
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.hospitalId = hospitalId;
        this.sourceReference = sourceReference;
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

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }
}
