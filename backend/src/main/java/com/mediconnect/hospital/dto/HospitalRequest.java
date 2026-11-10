package com.mediconnect.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class HospitalRequest {

    @NotBlank(message = "Hospital name is required")
    @Size(max = 200, message = "Hospital name cannot exceed 200 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 1000, message = "Address cannot exceed 1000 characters")
    private String address;

    @Size(max = 40, message = "Phone cannot exceed 40 characters")
    private String phone;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Size(max = 120, message = "Source name cannot exceed 120 characters")
    private String sourceName;

    @Size(max = 1000, message = "Source reference cannot exceed 1000 characters")
    private String sourceReference;

    public HospitalRequest() {}

    public HospitalRequest(String name, String address, String phone,
                           BigDecimal latitude, BigDecimal longitude,
                           String sourceName, String sourceReference) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sourceName = sourceName;
        this.sourceReference = sourceReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }
}
