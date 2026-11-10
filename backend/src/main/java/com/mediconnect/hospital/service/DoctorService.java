package com.mediconnect.hospital.service;

import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorRequest;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.entity.Doctor;
import com.mediconnect.hospital.entity.Hospital;
import com.mediconnect.hospital.repository.DoctorRepository;
import com.mediconnect.hospital.repository.HospitalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;

    public DoctorService(DoctorRepository doctorRepository, HospitalRepository hospitalRepository) {
        this.doctorRepository = doctorRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public DoctorResponse createDoctor(DoctorRequest request) {
        Hospital hospital = null;
        if (request.getHospitalId() != null) {
            hospital = hospitalRepository.findById(request.getHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));
        }

        Doctor doctor = new Doctor();
        doctor.setName(request.getName().trim());
        doctor.setSpecialization(request.getSpecialization().trim());
        doctor.setContactInfo(request.getContactInfo() != null ? request.getContactInfo().trim() : null);
        doctor.setSourceReference(request.getSourceReference() != null ? request.getSourceReference().trim() : null);
        doctor.setHospital(hospital);
        doctor.setVerifiedAt(OffsetDateTime.now());

        Doctor saved = doctorRepository.save(doctor);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return toResponse(doctor);
    }

    public DoctorResponse updateDoctor(UUID id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        Hospital hospital = null;
        if (request.getHospitalId() != null) {
            hospital = hospitalRepository.findById(request.getHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));
        }

        doctor.setName(request.getName().trim());
        doctor.setSpecialization(request.getSpecialization().trim());
        doctor.setContactInfo(request.getContactInfo() != null ? request.getContactInfo().trim() : null);
        doctor.setSourceReference(request.getSourceReference() != null ? request.getSourceReference().trim() : null);
        doctor.setHospital(hospital);
        doctor.setVerifiedAt(OffsetDateTime.now());

        Doctor updated = doctorRepository.save(doctor);
        return toResponse(updated);
    }

    public void deleteDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }

    @Transactional(readOnly = true)
    public PagedResponse<DoctorResponse> searchDoctors(String specialization, UUID hospitalId, String name, Pageable pageable) {
        String trimmedSpec = (specialization != null && !specialization.trim().isEmpty()) ? specialization.trim() : null;
        String trimmedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;

        Page<Doctor> page = doctorRepository.searchDoctors(trimmedSpec, hospitalId, trimmedName, pageable);
        Page<DoctorResponse> dtoPage = page.map(this::toResponse);
        return PagedResponse.fromPage(dtoPage);
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByHospitalId(UUID hospitalId) {
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new ResourceNotFoundException("Hospital not found with id: " + hospitalId);
        }
        return doctorRepository.findByHospitalId(hospitalId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse toResponse(Doctor doctor) {
        UUID hospitalId = doctor.getHospital() != null ? doctor.getHospital().getId() : null;
        String hospitalName = doctor.getHospital() != null ? doctor.getHospital().getName() : null;

        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getContactInfo(),
                hospitalId,
                hospitalName,
                doctor.getSourceReference(),
                doctor.getVerifiedAt()
        );
    }
}
