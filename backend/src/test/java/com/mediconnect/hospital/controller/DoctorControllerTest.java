package com.mediconnect.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediconnect.common.exception.GlobalExceptionHandler;
import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.dto.DoctorRequest;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.service.DoctorService;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private UUID doctorId;
    private UUID hospitalId;
    private DoctorResponse sampleDoctorResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        doctorId = UUID.randomUUID();
        hospitalId = UUID.randomUUID();
        sampleDoctorResponse = new DoctorResponse(
                doctorId,
                "Dr. Jennifer Gray",
                "Dermatology",
                "+1-555-7788",
                hospitalId,
                "Central Hospital",
                "DOC-REF-909",
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("GET /api/v1/doctors filters doctors by specialization and returns 200")
    void searchDoctors_returns200() throws Exception {
        PagedResponse<DoctorResponse> pagedResponse = new PagedResponse<>(
                List.of(sampleDoctorResponse), 0, 10, 1, 1, true
        );

        when(doctorService.searchDoctors(eq("Dermatology"), any(), any(), any(Pageable.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/doctors")
                        .param("specialization", "Dermatology")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].name").value("Dr. Jennifer Gray"))
                .andExpect(jsonPath("$.data.content[0].specialization").value("Dermatology"));
    }

    @Test
    @DisplayName("GET /api/v1/doctors/{id} returns doctor details")
    void getDoctorById_returns200() throws Exception {
        when(doctorService.getDoctorById(doctorId)).thenReturn(sampleDoctorResponse);

        mockMvc.perform(get("/api/v1/doctors/" + doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(doctorId.toString()))
                .andExpect(jsonPath("$.data.name").value("Dr. Jennifer Gray"));
    }

    @Test
    @DisplayName("GET /api/v1/doctors/{id} returns 404 when doctor not found")
    void getDoctorById_notFound_returns404() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(doctorService.getDoctorById(unknownId))
                .thenThrow(new ResourceNotFoundException("Doctor not found with id: " + unknownId));

        mockMvc.perform(get("/api/v1/doctors/" + unknownId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    @DisplayName("POST /api/v1/doctors creates doctor and returns 201")
    void createDoctor_returns201() throws Exception {
        DoctorRequest request = new DoctorRequest(
                "Dr. Jennifer Gray", "Dermatology", "+1-555-7788", hospitalId, "DOC-REF-909"
        );

        when(doctorService.createDoctor(any(DoctorRequest.class))).thenReturn(sampleDoctorResponse);

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Dr. Jennifer Gray"));
    }

    @Test
    @DisplayName("POST /api/v1/doctors fails validation on blank specialization")
    void createDoctor_blankSpecialization_returns400() throws Exception {
        DoctorRequest request = new DoctorRequest(
                "Dr. Name", "", null, null, null
        );

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("PUT /api/v1/doctors/{id} updates doctor and returns 200")
    void updateDoctor_returns200() throws Exception {
        DoctorRequest request = new DoctorRequest(
                "Dr. Jennifer Gray Updated", "Dermatology", "+1-555-7788", hospitalId, "DOC-REF-909"
        );

        when(doctorService.updateDoctor(eq(doctorId), any(DoctorRequest.class)))
                .thenReturn(sampleDoctorResponse);

        mockMvc.perform(put("/api/v1/doctors/" + doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("DELETE /api/v1/doctors/{id} deletes doctor and returns 200")
    void deleteDoctor_returns200() throws Exception {
        doNothing().when(doctorService).deleteDoctor(doctorId);

        mockMvc.perform(delete("/api/v1/doctors/" + doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Doctor deleted successfully"));
    }
}
