package com.mediconnect.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediconnect.common.exception.GlobalExceptionHandler;
import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.dto.HospitalRequest;
import com.mediconnect.hospital.dto.HospitalResponse;
import com.mediconnect.hospital.service.DoctorService;
import com.mediconnect.hospital.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HospitalControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private HospitalService hospitalService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private HospitalController hospitalController;

    private UUID hospitalId;
    private HospitalResponse sampleHospitalResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(hospitalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        hospitalId = UUID.randomUUID();
        sampleHospitalResponse = new HospitalResponse(
                hospitalId,
                "Central Hospital",
                "789 Health Boulevard",
                "+1-555-4321",
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060"),
                "State Health Dept",
                "REF-HOSP-789",
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("GET /api/v1/hospitals returns paginated hospital list")
    void searchHospitals_returns200() throws Exception {
        PagedResponse<HospitalResponse> pagedResponse = new PagedResponse<>(
                List.of(sampleHospitalResponse), 0, 10, 1, 1, true
        );

        when(hospitalService.searchHospitals(any(), any(), any(Pageable.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/hospitals")
                        .param("name", "Central")
                        .param("location", "Boulevard")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].name").value("Central Hospital"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/hospitals/{id} returns hospital details")
    void getHospitalById_returns200() throws Exception {
        when(hospitalService.getHospitalById(hospitalId)).thenReturn(sampleHospitalResponse);

        mockMvc.perform(get("/api/v1/hospitals/" + hospitalId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(hospitalId.toString()))
                .andExpect(jsonPath("$.data.name").value("Central Hospital"));
    }

    @Test
    @DisplayName("GET /api/v1/hospitals/{id} returns 404 when hospital not found")
    void getHospitalById_notFound_returns404() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(hospitalService.getHospitalById(unknownId))
                .thenThrow(new ResourceNotFoundException("Hospital not found with id: " + unknownId));

        mockMvc.perform(get("/api/v1/hospitals/" + unknownId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    @DisplayName("GET /api/v1/hospitals/{id}/doctors returns doctors in hospital")
    void getDoctorsByHospital_returns200() throws Exception {
        DoctorResponse docResponse = new DoctorResponse(
                UUID.randomUUID(), "Dr. Adams", "Neurology", "+1-555-1122",
                hospitalId, "Central Hospital", "REF-DOC", OffsetDateTime.now()
        );

        when(doctorService.getDoctorsByHospitalId(hospitalId)).thenReturn(List.of(docResponse));

        mockMvc.perform(get("/api/v1/hospitals/" + hospitalId + "/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Dr. Adams"))
                .andExpect(jsonPath("$.data[0].specialization").value("Neurology"));
    }

    @Test
    @DisplayName("POST /api/v1/hospitals creates hospital and returns 201")
    void createHospital_returns201() throws Exception {
        HospitalRequest request = new HospitalRequest(
                "Central Hospital", "789 Health Boulevard", "+1-555-4321",
                new BigDecimal("40.7128"), new BigDecimal("-74.0060"),
                "State Health Dept", "REF-HOSP-789"
        );

        when(hospitalService.createHospital(any(HospitalRequest.class))).thenReturn(sampleHospitalResponse);

        mockMvc.perform(post("/api/v1/hospitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Central Hospital"));
    }

    @Test
    @DisplayName("POST /api/v1/hospitals returns 400 when name is blank")
    void createHospital_blankName_returns400() throws Exception {
        HospitalRequest request = new HospitalRequest(
                "", "789 Health Boulevard", "+1-555-4321",
                null, null, null, null
        );

        mockMvc.perform(post("/api/v1/hospitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("PUT /api/v1/hospitals/{id} updates hospital and returns 200")
    void updateHospital_returns200() throws Exception {
        HospitalRequest request = new HospitalRequest(
                "Central Hospital Updated", "789 Health Boulevard", "+1-555-4321",
                null, null, null, null
        );

        when(hospitalService.updateHospital(eq(hospitalId), any(HospitalRequest.class)))
                .thenReturn(sampleHospitalResponse);

        mockMvc.perform(put("/api/v1/hospitals/" + hospitalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("DELETE /api/v1/hospitals/{id} deletes hospital and returns 200")
    void deleteHospital_returns200() throws Exception {
        doNothing().when(hospitalService).deleteHospital(hospitalId);

        mockMvc.perform(delete("/api/v1/hospitals/" + hospitalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Hospital deleted successfully"));
    }
}
