package com.mediconnect.hospital.service;

import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorRequest;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.entity.Doctor;
import com.mediconnect.hospital.entity.Hospital;
import com.mediconnect.hospital.repository.DoctorRepository;
import com.mediconnect.hospital.repository.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Hospital sampleHospital;
    private Doctor sampleDoctor;
    private UUID doctorId;
    private UUID hospitalId;

    @BeforeEach
    void setUp() {
        hospitalId = UUID.randomUUID();
        sampleHospital = new Hospital();
        sampleHospital.setId(hospitalId);
        sampleHospital.setName("St. Jude Hospital");

        doctorId = UUID.randomUUID();
        sampleDoctor = new Doctor(
                doctorId,
                sampleHospital,
                "Dr. Sarah Connor",
                "Cardiology",
                "+1-555-0344",
                "DOC-REF-101",
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("createDoctor successfully associates doctor with existing hospital")
    void createDoctor_withHospital_success() {
        DoctorRequest request = new DoctorRequest(
                "Dr. John Watson",
                "Internal Medicine",
                "+1-555-0789",
                hospitalId,
                "REF-889"
        );

        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> {
            Doctor d = inv.getArgument(0);
            d.setId(UUID.randomUUID());
            return d;
        });

        DoctorResponse response = doctorService.createDoctor(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Dr. John Watson", response.getName());
        assertEquals("Internal Medicine", response.getSpecialization());
        assertEquals(hospitalId, response.getHospitalId());
        assertEquals("St. Jude Hospital", response.getHospitalName());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    @DisplayName("createDoctor without hospital creates independent doctor")
    void createDoctor_independent_success() {
        DoctorRequest request = new DoctorRequest(
                "Dr. Gregory House",
                "Infectious Disease",
                "+1-555-0001",
                null,
                "REF-INDEP"
        );

        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> {
            Doctor d = inv.getArgument(0);
            d.setId(UUID.randomUUID());
            return d;
        });

        DoctorResponse response = doctorService.createDoctor(request);

        assertNotNull(response);
        assertNull(response.getHospitalId());
        assertNull(response.getHospitalName());
        assertEquals("Dr. Gregory House", response.getName());
    }

    @Test
    @DisplayName("createDoctor throws ResourceNotFoundException when hospital ID is invalid")
    void createDoctor_invalidHospital_throwsException() {
        UUID unknownHospitalId = UUID.randomUUID();
        DoctorRequest request = new DoctorRequest("Name", "Spec", null, unknownHospitalId, null);

        when(hospitalRepository.findById(unknownHospitalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.createDoctor(request));
        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("getDoctorById returns doctor when found")
    void getDoctorById_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(sampleDoctor));

        DoctorResponse response = doctorService.getDoctorById(doctorId);

        assertNotNull(response);
        assertEquals(doctorId, response.getId());
        assertEquals("Dr. Sarah Connor", response.getName());
        assertEquals("Cardiology", response.getSpecialization());
    }

    @Test
    @DisplayName("getDoctorById throws ResourceNotFoundException when not found")
    void getDoctorById_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(doctorRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(unknownId));
    }

    @Test
    @DisplayName("updateDoctor updates doctor details and re-associates hospital")
    void updateDoctor_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(sampleDoctor));
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(sampleDoctor);

        DoctorRequest request = new DoctorRequest(
                "Dr. Sarah Connor-Smith",
                "Pediatric Cardiology",
                "+1-555-9988",
                hospitalId,
                "REF-UPDATED"
        );

        DoctorResponse response = doctorService.updateDoctor(doctorId, request);

        assertNotNull(response);
        assertEquals("Dr. Sarah Connor-Smith", response.getName());
        assertEquals("Pediatric Cardiology", response.getSpecialization());
        verify(doctorRepository).save(sampleDoctor);
    }

    @Test
    @DisplayName("deleteDoctor deletes doctor when found")
    void deleteDoctor_success() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(sampleDoctor));

        doctorService.deleteDoctor(doctorId);

        verify(doctorRepository).delete(sampleDoctor);
    }

    @Test
    @DisplayName("searchDoctors filters by specialization, hospital, and name")
    void searchDoctors_withFilters_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> pagedResult = new PageImpl<>(List.of(sampleDoctor), pageable, 1);

        when(doctorRepository.searchDoctors(eq("Cardiology"), eq(hospitalId), eq("Sarah"), eq(pageable)))
                .thenReturn(pagedResult);

        PagedResponse<DoctorResponse> response = doctorService.searchDoctors("Cardiology", hospitalId, "Sarah", pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Dr. Sarah Connor", response.getContent().get(0).getName());
        assertEquals("Cardiology", response.getContent().get(0).getSpecialization());
    }

    @Test
    @DisplayName("getDoctorsByHospitalId returns list of doctors for hospital")
    void getDoctorsByHospitalId_success() {
        when(hospitalRepository.existsById(hospitalId)).thenReturn(true);
        when(doctorRepository.findByHospitalId(hospitalId)).thenReturn(List.of(sampleDoctor));

        List<DoctorResponse> doctors = doctorService.getDoctorsByHospitalId(hospitalId);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        assertEquals("Dr. Sarah Connor", doctors.get(0).getName());
    }

    @Test
    @DisplayName("getDoctorsByHospitalId throws ResourceNotFoundException if hospital does not exist")
    void getDoctorsByHospitalId_notFound() {
        UUID unknownHospitalId = UUID.randomUUID();
        when(hospitalRepository.existsById(unknownHospitalId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorsByHospitalId(unknownHospitalId));
    }
}
