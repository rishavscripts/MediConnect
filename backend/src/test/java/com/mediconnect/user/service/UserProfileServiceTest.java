package com.mediconnect.user.service;

import com.mediconnect.auth.entity.Role;
import com.mediconnect.auth.entity.User;
import com.mediconnect.auth.repository.UserRepository;
import com.mediconnect.common.exception.ResourceNotFoundException;
import com.mediconnect.common.exception.UnauthorizedException;
import com.mediconnect.user.dto.*;
import com.mediconnect.user.entity.Allergy;
import com.mediconnect.user.entity.MedicalCondition;
import com.mediconnect.user.entity.UserProfile;
import com.mediconnect.user.repository.AllergyRepository;
import com.mediconnect.user.repository.MedicalConditionRepository;
import com.mediconnect.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private MedicalConditionRepository medicalConditionRepository;

    @Mock
    private AllergyRepository allergyRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User patientA;
    private User patientB;
    private UserProfile profileA;

    @BeforeEach
    void setUp() {
        patientA = new User(UUID.randomUUID(), "Patient One", "patient1@example.com", "hashed_pw", Role.PATIENT, true);
        patientB = new User(UUID.randomUUID(), "Patient Two", "patient2@example.com", "hashed_pw", Role.PATIENT, true);

        profileA = new UserProfile(patientA);
        profileA.setId(UUID.randomUUID());
        profileA.setPhone("+1234567890");
        profileA.setAddress("123 Healthcare Ave");
        profileA.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("getProfile returns user profile when profile exists")
    void getProfile_success() {
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(userProfileRepository.findByUser(patientA)).thenReturn(Optional.of(profileA));

        UserProfileResponse response = userProfileService.getProfile("patient1@example.com");

        assertNotNull(response);
        assertEquals(patientA.getId(), response.getUserId());
        assertEquals(patientA.getEmail(), response.getEmail());
        assertEquals("+1234567890", response.getPhone());
    }

    @Test
    @DisplayName("getProfile creates new profile if one does not exist")
    void getProfile_autoCreatesIfAbsent() {
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(userProfileRepository.findByUser(patientA)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> {
            UserProfile p = inv.getArgument(0);
            p.setId(UUID.randomUUID());
            return p;
        });

        UserProfileResponse response = userProfileService.getProfile("patient1@example.com");

        assertNotNull(response);
        assertEquals(patientA.getId(), response.getUserId());
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("updateProfile updates user and profile fields")
    void updateProfile_success() {
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(userProfileRepository.findByUser(patientA)).thenReturn(Optional.of(profileA));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profileA);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Updated Name");
        request.setPhone("+9876543210");
        request.setAddress("456 New Road");
        request.setDateOfBirth(LocalDate.of(1992, 5, 10));

        UserProfileResponse response = userProfileService.updateProfile("patient1@example.com", request);

        assertEquals("Updated Name", patientA.getFullName());
        assertEquals("+9876543210", profileA.getPhone());
        assertEquals("456 New Road", profileA.getAddress());
        verify(userRepository).save(patientA);
        verify(userProfileRepository).save(profileA);
    }

    @Test
    @DisplayName("getMedicalProfile returns conditions and allergies for authenticated user")
    void getMedicalProfile_success() {
        MedicalCondition condition = new MedicalCondition(patientA, "Hypertension", "Monitor daily");
        condition.setId(UUID.randomUUID());

        Allergy allergy = new Allergy(patientA, "Penicillin", "Rash and hives");
        allergy.setId(UUID.randomUUID());

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.findAllByUserOrderByRecordedAtDesc(patientA)).thenReturn(List.of(condition));
        when(allergyRepository.findAllByUserOrderByRecordedAtDesc(patientA)).thenReturn(List.of(allergy));

        MedicalProfileResponse response = userProfileService.getMedicalProfile("patient1@example.com");

        assertNotNull(response);
        assertEquals(1, response.getConditions().size());
        assertEquals("Hypertension", response.getConditions().get(0).getConditionName());
        assertEquals(1, response.getAllergies().size());
        assertEquals("Penicillin", response.getAllergies().get(0).getSubstance());
    }

    // --- Conditions Tests ---

    @Test
    @DisplayName("addCondition successfully adds condition for user")
    void addCondition_success() {
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.save(any(MedicalCondition.class))).thenAnswer(inv -> {
            MedicalCondition c = inv.getArgument(0);
            c.setId(UUID.randomUUID());
            return c;
        });

        MedicalConditionRequest request = new MedicalConditionRequest("Asthma", "Mild seasonal");
        MedicalConditionResponse response = userProfileService.addCondition("patient1@example.com", request);

        assertNotNull(response);
        assertEquals("Asthma", response.getConditionName());
        assertEquals("Mild seasonal", response.getNotes());
        verify(medicalConditionRepository).save(any(MedicalCondition.class));
    }

    @Test
    @DisplayName("updateCondition successfully updates condition when owned by user")
    void updateCondition_success() {
        UUID conditionId = UUID.randomUUID();
        MedicalCondition condition = new MedicalCondition(patientA, "Asthma", "Old notes");
        condition.setId(conditionId);

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.findById(conditionId)).thenReturn(Optional.of(condition));
        when(medicalConditionRepository.save(any(MedicalCondition.class))).thenReturn(condition);

        MedicalConditionRequest request = new MedicalConditionRequest("Asthma (Severe)", "Updated notes");
        MedicalConditionResponse response = userProfileService.updateCondition("patient1@example.com", conditionId, request);

        assertEquals("Asthma (Severe)", response.getConditionName());
        assertEquals("Updated notes", response.getNotes());
    }

    @Test
    @DisplayName("updateCondition throws UnauthorizedException when user does not own condition")
    void updateCondition_crossUserOwnership_throwsUnauthorizedException() {
        UUID conditionId = UUID.randomUUID();
        // Condition belongs to patientB
        MedicalCondition condition = new MedicalCondition(patientB, "Asthma", "Patient B notes");
        condition.setId(conditionId);

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.findById(conditionId)).thenReturn(Optional.of(condition));

        MedicalConditionRequest request = new MedicalConditionRequest("Tampered", "Hacked");

        assertThrows(UnauthorizedException.class, () ->
                userProfileService.updateCondition("patient1@example.com", conditionId, request)
        );
        verify(medicalConditionRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteCondition throws UnauthorizedException when user does not own condition")
    void deleteCondition_crossUserOwnership_throwsUnauthorizedException() {
        UUID conditionId = UUID.randomUUID();
        MedicalCondition condition = new MedicalCondition(patientB, "Diabetes", "Patient B notes");
        condition.setId(conditionId);

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.findById(conditionId)).thenReturn(Optional.of(condition));

        assertThrows(UnauthorizedException.class, () ->
                userProfileService.deleteCondition("patient1@example.com", conditionId)
        );
        verify(medicalConditionRepository, never()).delete(any());
    }

    // --- Allergies Tests ---

    @Test
    @DisplayName("addAllergy successfully adds allergy for user")
    void addAllergy_success() {
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(allergyRepository.save(any(Allergy.class))).thenAnswer(inv -> {
            Allergy a = inv.getArgument(0);
            a.setId(UUID.randomUUID());
            return a;
        });

        AllergyRequest request = new AllergyRequest("Peanuts", "Severe anaphylaxis");
        AllergyResponse response = userProfileService.addAllergy("patient1@example.com", request);

        assertNotNull(response);
        assertEquals("Peanuts", response.getSubstance());
        assertEquals("Severe anaphylaxis", response.getReactionNotes());
        verify(allergyRepository).save(any(Allergy.class));
    }

    @Test
    @DisplayName("updateAllergy throws UnauthorizedException when user does not own allergy")
    void updateAllergy_crossUserOwnership_throwsUnauthorizedException() {
        UUID allergyId = UUID.randomUUID();
        // Allergy belongs to patientB
        Allergy allergy = new Allergy(patientB, "Latex", "Patient B allergy");
        allergy.setId(allergyId);

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.of(allergy));

        AllergyRequest request = new AllergyRequest("Tampered", "Notes");

        assertThrows(UnauthorizedException.class, () ->
                userProfileService.updateAllergy("patient1@example.com", allergyId, request)
        );
        verify(allergyRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteAllergy throws UnauthorizedException when user does not own allergy")
    void deleteAllergy_crossUserOwnership_throwsUnauthorizedException() {
        UUID allergyId = UUID.randomUUID();
        Allergy allergy = new Allergy(patientB, "Shellfish", "Patient B allergy");
        allergy.setId(allergyId);

        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.of(allergy));

        assertThrows(UnauthorizedException.class, () ->
                userProfileService.deleteAllergy("patient1@example.com", allergyId)
        );
        verify(allergyRepository, never()).delete(any());
    }

    @Test
    @DisplayName("updateCondition throws ResourceNotFoundException when record not found")
    void updateCondition_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(userRepository.findByEmail("patient1@example.com")).thenReturn(Optional.of(patientA));
        when(medicalConditionRepository.findById(unknownId)).thenReturn(Optional.empty());

        MedicalConditionRequest request = new MedicalConditionRequest("Asthma", "Notes");

        assertThrows(ResourceNotFoundException.class, () ->
                userProfileService.updateCondition("patient1@example.com", unknownId, request)
        );
    }
}
