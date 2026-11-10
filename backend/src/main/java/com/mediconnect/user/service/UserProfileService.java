package com.mediconnect.user.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MedicalConditionRepository medicalConditionRepository;
    private final AllergyRepository allergyRepository;

    public UserProfileService(UserRepository userRepository,
                              UserProfileRepository userProfileRepository,
                              MedicalConditionRepository medicalConditionRepository,
                              AllergyRepository allergyRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.medicalConditionRepository = medicalConditionRepository;
        this.allergyRepository = allergyRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String userEmail) {
        User user = getUserByEmail(userEmail);
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile(user);
                    return userProfileRepository.save(newProfile);
                });

        return toUserProfileResponse(user, profile);
    }

    public UserProfileResponse updateProfile(String userEmail, UpdateProfileRequest request) {
        User user = getUserByEmail(userEmail);

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName().trim());
            user.setUpdatedAt(OffsetDateTime.now());
            userRepository.save(user);
        }

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> new UserProfile(user));

        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        profile.setUpdatedAt(OffsetDateTime.now());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return toUserProfileResponse(user, savedProfile);
    }

    @Transactional(readOnly = true)
    public MedicalProfileResponse getMedicalProfile(String userEmail) {
        User user = getUserByEmail(userEmail);

        List<MedicalConditionResponse> conditions = medicalConditionRepository
                .findAllByUserOrderByRecordedAtDesc(user)
                .stream()
                .map(this::toConditionResponse)
                .collect(Collectors.toList());

        List<AllergyResponse> allergies = allergyRepository
                .findAllByUserOrderByRecordedAtDesc(user)
                .stream()
                .map(this::toAllergyResponse)
                .collect(Collectors.toList());

        return new MedicalProfileResponse(conditions, allergies);
    }

    // Medical Conditions
    @Transactional(readOnly = true)
    public List<MedicalConditionResponse> getConditions(String userEmail) {
        User user = getUserByEmail(userEmail);
        return medicalConditionRepository.findAllByUserOrderByRecordedAtDesc(user)
                .stream()
                .map(this::toConditionResponse)
                .collect(Collectors.toList());
    }

    public MedicalConditionResponse addCondition(String userEmail, MedicalConditionRequest request) {
        User user = getUserByEmail(userEmail);
        MedicalCondition condition = new MedicalCondition(
                user,
                request.getConditionName().trim(),
                request.getNotes() != null ? request.getNotes().trim() : null
        );
        MedicalCondition saved = medicalConditionRepository.save(condition);
        return toConditionResponse(saved);
    }

    public MedicalConditionResponse updateCondition(String userEmail, UUID conditionId, MedicalConditionRequest request) {
        User user = getUserByEmail(userEmail);
        MedicalCondition condition = medicalConditionRepository.findById(conditionId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical condition not found with id: " + conditionId));

        if (!condition.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied: You do not have permission to modify this medical condition");
        }

        condition.setConditionName(request.getConditionName().trim());
        if (request.getNotes() != null) {
            condition.setNotes(request.getNotes().trim());
        }
        MedicalCondition saved = medicalConditionRepository.save(condition);
        return toConditionResponse(saved);
    }

    public void deleteCondition(String userEmail, UUID conditionId) {
        User user = getUserByEmail(userEmail);
        MedicalCondition condition = medicalConditionRepository.findById(conditionId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical condition not found with id: " + conditionId));

        if (!condition.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied: You do not have permission to delete this medical condition");
        }

        medicalConditionRepository.delete(condition);
    }

    // Allergies
    @Transactional(readOnly = true)
    public List<AllergyResponse> getAllergies(String userEmail) {
        User user = getUserByEmail(userEmail);
        return allergyRepository.findAllByUserOrderByRecordedAtDesc(user)
                .stream()
                .map(this::toAllergyResponse)
                .collect(Collectors.toList());
    }

    public AllergyResponse addAllergy(String userEmail, AllergyRequest request) {
        User user = getUserByEmail(userEmail);
        Allergy allergy = new Allergy(
                user,
                request.getSubstance().trim(),
                request.getReactionNotes() != null ? request.getReactionNotes().trim() : null
        );
        Allergy saved = allergyRepository.save(allergy);
        return toAllergyResponse(saved);
    }

    public AllergyResponse updateAllergy(String userEmail, UUID allergyId, AllergyRequest request) {
        User user = getUserByEmail(userEmail);
        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + allergyId));

        if (!allergy.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied: You do not have permission to modify this allergy record");
        }

        allergy.setSubstance(request.getSubstance().trim());
        if (request.getReactionNotes() != null) {
            allergy.setReactionNotes(request.getReactionNotes().trim());
        }
        Allergy saved = allergyRepository.save(allergy);
        return toAllergyResponse(saved);
    }

    public void deleteAllergy(String userEmail, UUID allergyId) {
        User user = getUserByEmail(userEmail);
        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + allergyId));

        if (!allergy.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied: You do not have permission to delete this allergy record");
        }

        allergyRepository.delete(allergy);
    }

    // Helper mappings
    private UserProfileResponse toUserProfileResponse(User user, UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getDateOfBirth(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    private MedicalConditionResponse toConditionResponse(MedicalCondition condition) {
        return new MedicalConditionResponse(
                condition.getId(),
                condition.getConditionName(),
                condition.getNotes(),
                condition.getRecordedAt()
        );
    }

    private AllergyResponse toAllergyResponse(Allergy allergy) {
        return new AllergyResponse(
                allergy.getId(),
                allergy.getSubstance(),
                allergy.getReactionNotes(),
                allergy.getRecordedAt()
        );
    }
}
