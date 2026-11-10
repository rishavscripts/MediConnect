package com.mediconnect.hospital.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediconnect.config.SecurityConfig;
import com.mediconnect.hospital.controller.DoctorController;
import com.mediconnect.hospital.controller.HospitalController;
import com.mediconnect.hospital.dto.DoctorRequest;
import com.mediconnect.hospital.dto.DoctorResponse;
import com.mediconnect.hospital.dto.HospitalRequest;
import com.mediconnect.hospital.dto.HospitalResponse;
import com.mediconnect.common.response.PagedResponse;
import com.mediconnect.hospital.service.DoctorService;
import com.mediconnect.hospital.service.HospitalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {HospitalController.class, DoctorController.class})
@Import(SecurityConfig.class)
class HospitalDoctorSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HospitalService hospitalService;

    @MockBean
    private DoctorService doctorService;

    @Test
    @DisplayName("Public (anonymous) user can search hospitals")
    void anonymous_canSearchHospitals() throws Exception {
        when(hospitalService.searchHospitals(any(), any(), any()))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0, true));

        mockMvc.perform(get("/api/v1/hospitals"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public (anonymous) user can search doctors")
    void anonymous_canSearchDoctors() throws Exception {
        when(doctorService.searchDoctors(any(), any(), any(), any()))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0, true));

        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    @DisplayName("PATIENT role user can search and view hospitals")
    void patient_canSearchHospitals() throws Exception {
        when(hospitalService.searchHospitals(any(), any(), any()))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0, true));

        mockMvc.perform(get("/api/v1/hospitals"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    @DisplayName("PATIENT role user cannot create a hospital (403 Forbidden)")
    void patient_cannotCreateHospital() throws Exception {
        HospitalRequest request = new HospitalRequest("Hospital", "Address", null, null, null, null, null);

        mockMvc.perform(post("/api/v1/hospitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    @DisplayName("PATIENT role user cannot delete a hospital (403 Forbidden)")
    void patient_cannotDeleteHospital() throws Exception {
        mockMvc.perform(delete("/api/v1/hospitals/" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    @DisplayName("PATIENT role user cannot create a doctor (403 Forbidden)")
    void patient_cannotCreateDoctor() throws Exception {
        DoctorRequest request = new DoctorRequest("Dr. Test", "Cardiology", null, null, null);

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("ADMIN role user can create a hospital")
    void admin_canCreateHospital() throws Exception {
        HospitalRequest request = new HospitalRequest("Admin Hospital", "Main Rd", null, null, null, null, null);
        HospitalResponse response = new HospitalResponse(UUID.randomUUID(), "Admin Hospital", "Main Rd", null, null, null, null, null, null);

        when(hospitalService.createHospital(any(HospitalRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/hospitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("ADMIN role user can create a doctor")
    void admin_canCreateDoctor() throws Exception {
        DoctorRequest request = new DoctorRequest("Dr. Admin", "Pediatrics", null, null, null);
        DoctorResponse response = new DoctorResponse(UUID.randomUUID(), "Dr. Admin", "Pediatrics", null, null, null, null, null);

        when(doctorService.createDoctor(any(DoctorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
