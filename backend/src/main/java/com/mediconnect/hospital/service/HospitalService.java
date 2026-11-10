package com.mediconnect.hospital.service;

import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.HospitalRequest;
import com.mediconnect.hospital.dto.HospitalResponse;
import com.mediconnect.hospital.entity.Hospital;
import com.mediconnect.hospital.repository.HospitalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public HospitalResponse createHospital(HospitalRequest request) {
        Hospital hospital = new Hospital();
        mapRequestToEntity(request, hospital);
        hospital.setVerifiedAt(OffsetDateTime.now());

        Hospital saved = hospitalRepository.save(hospital);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public HospitalResponse getHospitalById(UUID id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        return toResponse(hospital);
    }

    public HospitalResponse updateHospital(UUID id, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));

        mapRequestToEntity(request, hospital);
        hospital.setVerifiedAt(OffsetDateTime.now());

        Hospital updated = hospitalRepository.save(hospital);
        return toResponse(updated);
    }

    public void deleteHospital(UUID id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        hospitalRepository.delete(hospital);
    }

    @Transactional(readOnly = true)
    public PagedResponse<HospitalResponse> searchHospitals(String name, String location, Pageable pageable) {
        String trimmedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String trimmedLocation = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        Page<Hospital> page = hospitalRepository.searchHospitals(trimmedName, trimmedLocation, pageable);
        Page<HospitalResponse> dtoPage = page.map(this::toResponse);
        return PagedResponse.fromPage(dtoPage);
    }

    private void mapRequestToEntity(HospitalRequest request, Hospital hospital) {
        hospital.setName(request.getName().trim());
        hospital.setAddress(request.getAddress().trim());
        hospital.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        hospital.setLatitude(request.getLatitude());
        hospital.setLongitude(request.getLongitude());
        hospital.setSourceName(request.getSourceName() != null ? request.getSourceName().trim() : null);
        hospital.setSourceReference(request.getSourceReference() != null ? request.getSourceReference().trim() : null);
    }

    public HospitalResponse toResponse(Hospital hospital) {
        return new HospitalResponse(
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getPhone(),
                hospital.getLatitude(),
                hospital.getLongitude(),
                hospital.getSourceName(),
                hospital.getSourceReference(),
                hospital.getVerifiedAt()
        );
    }
}
