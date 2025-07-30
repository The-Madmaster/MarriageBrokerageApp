package com.marriagebureau.clientmanagement.service;

import com.marriagebureau.clientmanagement.dto.CreateProfileRequest;
import com.marriagebureau.clientmanagement.dto.ProfileResponse;
import com.marriagebureau.clientmanagement.dto.UpdateProfileRequest;
import com.marriagebureau.clientmanagement.model.Profile; // Assuming Profile is moved to clientmanagement.model
import com.marriagebureau.clientmanagement.repository.ProfileRepository;
import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    /**
     * Creates a new client profile and assigns it to the currently authenticated broker.
     * This method is called by the ClientProfileController.
     */
    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        // 1. Get the currently authenticated broker.
        AppUser broker = getAuthenticatedBroker();

        // 2. Build the profile entity from the request DTO.
        Profile profile = Profile.builder()
                .broker(broker) // Link the profile to the broker
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .heightCm(request.getHeightCm())
                .religion(request.getReligion())
                .caste(request.getCaste())
                // ... map all other fields from the request ...
                .isActive(true)
                .build();

        // 3. Save the profile and convert it to a response DTO.
        Profile savedProfile = profileRepository.save(profile);
        return convertToDto(savedProfile);
    }

    /**
     * Retrieves all client profiles that belong to the currently authenticated broker.
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getClientsForAuthenticatedBroker() {
        AppUser broker = getAuthenticatedBroker();
        return profileRepository.findAllByBroker(broker)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single client profile by its ID, ensuring it belongs to the authenticated broker.
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfileByIdForBroker(Long profileId) {
        Profile profile = getProfileAndVerifyOwnership(profileId);
        return convertToDto(profile);
    }

    /**
     * Updates an existing client profile, ensuring it belongs to the authenticated broker.
     */
    @Transactional
    public ProfileResponse updateProfile(Long profileId, UpdateProfileRequest request) {
        // This method also implicitly verifies ownership.
        Profile profile = getProfileAndVerifyOwnership(profileId);

        // Update fields using the existing Optional-based approach
        Optional.ofNullable(request.getFullName()).ifPresent(profile::setFullName);
        Optional.ofNullable(request.getDateOfBirth()).ifPresent(profile::setDateOfBirth);
        // ... continue mapping all other updatable fields ...

        Profile updatedProfile = profileRepository.save(profile);
        return convertToDto(updatedProfile);
    }

    /**
     * Deletes a client profile, ensuring it belongs to the authenticated broker.
     */
    @Transactional
    public void deleteProfile(Long profileId) {
        // This method also implicitly verifies ownership.
        Profile profile = getProfileAndVerifyOwnership(profileId);
        profileRepository.delete(profile);
    }

    // --- HELPER METHODS ---

    /**
     * A private helper method to get the Profile and verify the authenticated broker is the owner.
     * This centralizes our security check.
     */
    private Profile getProfileAndVerifyOwnership(Long profileId) {
        AppUser broker = getAuthenticatedBroker();
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        // CRITICAL: Security check to ensure broker owns the profile.
        if (!profile.getBroker().getId().equals(broker.getId())) {
            throw new AccessDeniedException("You do not have permission to access this profile.");
        }
        return profile;
    }

    /**
     * A private helper method to get the currently authenticated AppUser (broker).
     */
    private AppUser getAuthenticatedBroker() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated broker not found with email: " + userEmail));
    }

    /**
     * Private helper to convert Profile entity to ProfileResponse DTO.
     * (Your existing convertToDto method is good, just ensure field names match)
     */
    private ProfileResponse convertToDto(Profile profile) {
        // Your existing mapping logic here...
        return ProfileResponse.builder()
                .id(profile.getId())
                .brokerId(profile.getBroker().getId()) // Use brokerId instead of appUserId
                .email(profile.getBroker().getEmail())
                // ... rest of the fields
                .build();
    }
}