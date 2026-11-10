package com.mediconnect.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediconnect.common.exception.GlobalExceptionHandler;
import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.exception.UnauthorizedException;
import com.mediconnect.user.dto.*;
import com.mediconnect.user.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    private final String testEmail = "patient@example.com";
    private Authentication auth;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userProfileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        auth = new UsernamePasswordAuthenticationToken(testEmail, "pw", Collections.emptyList());
    }

    @Test
    @DisplayName("GET /api/v1/users/me returns 200 with user profile")
    void getProfile_authenticated_returns200() throws Exception {
        UserProfileResponse response = new UserProfileResponse(
                UUID.randomUUID(), UUID.randomUUID(), "Jane Doe", testEmail,
                "+123456789", "123 Street", LocalDate.of(1995, 3, 15),
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        when(userProfileService.getProfile(testEmail)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/me").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(testEmail))
                .andExpect(jsonPath("$.data.fullName").value("Jane Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/users/me returns 403 Forbidden when unauthenticated")
    void getProfile_unauthenticated_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @DisplayName("PUT /api/v1/users/me updates profile successfully")
    void updateProfile_success() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Jane Updated");
        request.setPhone("+987654321");
        request.setDateOfBirth(LocalDate.of(1995, 3, 15));

        UserProfileResponse response = new UserProfileResponse(
                UUID.randomUUID(), UUID.randomUUID(), "Jane Updated", testEmail,
                "+987654321", "123 Street", LocalDate.of(1995, 3, 15),
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        when(userProfileService.updateProfile(eq(testEmail), any(UpdateProfileRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/users/me")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fullName").value("Jane Updated"));
    }

    @Test
    @DisplayName("PUT /api/v1/users/me returns 400 when date of birth is in the future")
    void updateProfile_invalidDateOfBirth_returns400() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setDateOfBirth(LocalDate.now().plusDays(10)); // Future date

        mockMvc.perform(put("/api/v1/users/me")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("POST /api/v1/users/me/conditions creates condition and returns 201")
    void addCondition_valid_returns201() throws Exception {
        MedicalConditionRequest request = new MedicalConditionRequest("Type 2 Diabetes", "Routine checkups");
        MedicalConditionResponse response = new MedicalConditionResponse(
                UUID.randomUUID(), "Type 2 Diabetes", "Routine checkups", OffsetDateTime.now()
        );

        when(userProfileService.addCondition(eq(testEmail), any(MedicalConditionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/users/me/conditions")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.conditionName").value("Type 2 Diabetes"));
    }

    @Test
    @DisplayName("POST /api/v1/users/me/conditions fails validation when name is blank")
    void addCondition_blankName_returns400() throws Exception {
        MedicalConditionRequest request = new MedicalConditionRequest("", "Notes");

        mockMvc.perform(post("/api/v1/users/me/conditions")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/me/conditions/{id} returns 200 on success")
    void deleteCondition_success() throws Exception {
        UUID conditionId = UUID.randomUUID();
        doNothing().when(userProfileService).deleteCondition(testEmail, conditionId);

        mockMvc.perform(delete("/api/v1/users/me/conditions/" + conditionId).principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/me/conditions/{id} returns 403 on ownership violation")
    void deleteCondition_crossUserAccess_returns403() throws Exception {
        UUID conditionId = UUID.randomUUID();
        doThrow(new UnauthorizedException("Access denied")).when(userProfileService).deleteCondition(testEmail, conditionId);

        mockMvc.perform(delete("/api/v1/users/me/conditions/" + conditionId).principal(auth))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @DisplayName("POST /api/v1/users/me/allergies fails validation when substance is blank")
    void addAllergy_blankSubstance_returns400() throws Exception {
        AllergyRequest request = new AllergyRequest("", "Reaction");

        mockMvc.perform(post("/api/v1/users/me/allergies")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("GET /api/v1/users/me/medical-profile returns combined medical summary")
    void getMedicalProfile_success() throws Exception {
        MedicalProfileResponse response = new MedicalProfileResponse(
                List.of(new MedicalConditionResponse(UUID.randomUUID(), "Asthma", "Mild", OffsetDateTime.now())),
                List.of(new AllergyResponse(UUID.randomUUID(), "Pollen", "Sneezing", OffsetDateTime.now()))
        );

        when(userProfileService.getMedicalProfile(testEmail)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/me/medical-profile").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.conditions[0].conditionName").value("Asthma"))
                .andExpect(jsonPath("$.data.allergies[0].substance").value("Pollen"));
    }
}
