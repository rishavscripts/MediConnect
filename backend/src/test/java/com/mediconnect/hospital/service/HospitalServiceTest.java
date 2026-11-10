package com.mediconnect.hospital.service;

import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.HospitalRequest;
import com.mediconnect.hospital.dto.HospitalResponse;
import com.mediconnect.hospital.entity.Hospital;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalService hospitalService;

    private Hospital sampleHospital;
    private UUID hospitalId;

    @BeforeEach
    void setUp() {
        hospitalId = UUID.randomUUID();
        sampleHospital = new Hospital(
                hospitalId,
                "City General Hospital",
                "100 Medical Center Way, Downtown",
                "+1-555-0199",
                new BigDecimal("40.712776"),
                new BigDecimal("-74.005974"),
                "National Registry",
                "REG-1002",
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("createHospital persists and returns hospital response")
    void createHospital_success() {
        HospitalRequest request = new HospitalRequest(
                "Metro Health Center",
                "200 Health Blvd",
                "+1-555-0200",
                new BigDecimal("40.730610"),
                new BigDecimal("-73.935242"),
                "State Registry",
                "REG-2001"
        );

        when(hospitalRepository.save(any(Hospital.class))).thenAnswer(inv -> {
            Hospital h = inv.getArgument(0);
            h.setId(UUID.randomUUID());
            return h;
        });

        HospitalResponse response = hospitalService.createHospital(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Metro Health Center", response.getName());
        assertEquals("200 Health Blvd", response.getAddress());
        assertNotNull(response.getVerifiedAt());
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    @DisplayName("getHospitalById returns hospital details when found")
    void getHospitalById_success() {
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(sampleHospital));

        HospitalResponse response = hospitalService.getHospitalById(hospitalId);

        assertNotNull(response);
        assertEquals(hospitalId, response.getId());
        assertEquals("City General Hospital", response.getName());
    }

    @Test
    @DisplayName("getHospitalById throws ResourceNotFoundException when not found")
    void getHospitalById_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(hospitalRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hospitalService.getHospitalById(unknownId));
    }

    @Test
    @DisplayName("updateHospital updates entity fields and persists changes")
    void updateHospital_success() {
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(sampleHospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(sampleHospital);

        HospitalRequest updateRequest = new HospitalRequest(
                "City General Hospital - Main Branch",
                "100 Medical Center Way, Suite 100",
                "+1-555-9999",
                new BigDecimal("40.712800"),
                new BigDecimal("-74.006000"),
                "Updated Source",
                "REG-1002-B"
        );

        HospitalResponse response = hospitalService.updateHospital(hospitalId, updateRequest);

        assertNotNull(response);
        assertEquals("City General Hospital - Main Branch", response.getName());
        assertEquals("+1-555-9999", response.getPhone());
        verify(hospitalRepository).save(sampleHospital);
    }

    @Test
    @DisplayName("updateHospital throws ResourceNotFoundException when hospital does not exist")
    void updateHospital_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(hospitalRepository.findById(unknownId)).thenReturn(Optional.empty());

        HospitalRequest request = new HospitalRequest("Name", "Address", null, null, null, null, null);

        assertThrows(ResourceNotFoundException.class, () -> hospitalService.updateHospital(unknownId, request));
        verify(hospitalRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteHospital removes hospital when found")
    void deleteHospital_success() {
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(sampleHospital));

        hospitalService.deleteHospital(hospitalId);

        verify(hospitalRepository).delete(sampleHospital);
    }

    @Test
    @DisplayName("deleteHospital throws ResourceNotFoundException when not found")
    void deleteHospital_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(hospitalRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hospitalService.deleteHospital(unknownId));
        verify(hospitalRepository, never()).delete(any());
    }

    @Test
    @DisplayName("searchHospitals returns paginated results matching query criteria")
    void searchHospitals_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hospital> pagedResult = new PageImpl<>(List.of(sampleHospital), pageable, 1);

        when(hospitalRepository.searchHospitals(eq("City"), eq("Downtown"), eq(pageable)))
                .thenReturn(pagedResult);

        PagedResponse<HospitalResponse> response = hospitalService.searchHospitals("City", "Downtown", pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("City General Hospital", response.getContent().get(0).getName());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
    }
}
