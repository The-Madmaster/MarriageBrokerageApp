package com.marriagebureau.usermanagement.service;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.entity.AppUser; // Import AppUser
import com.marriagebureau.usermanagement.repository.ProfileRepository;
import com.marriagebureau.usermanagement.repository.AppUserRepository; // Import AppUserRepository
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository; // Inject AppUserRepository

    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    // --- Create Profile ---
    // This method needs to link the profile to an existing AppUser
    public Profile createProfile(Long appUserId, Profile profile) {
        AppUser appUser = appUserRepository.findById(appUserId)
            .orElseThrow(() -> new RuntimeException("AppUser not found with id " + appUserId));
        profile.setAppUser(appUser); // Link the Profile to the AppUser object
        return profileRepository.save(profile);
    }

    // --- Get All Profiles ---
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // --- Get Profile by ID ---
    public Optional<Profile> getProfileById(Long profileId) {
        return profileRepository.findById(profileId);
    }

    // --- Update Profile ---
    @Transactional
    public Profile updateProfile(Long appUserId, Long profileId, Profile updatedProfile) {
        return profileRepository.findById(profileId).map(profile -> {
            // Ensure the appUser for this profile matches the appUserId from the path
            if (!profile.getAppUser().getId().equals(appUserId)) {
                throw new RuntimeException("Unauthorized: Profile does not belong to this user.");
            }

            // Update fields based on your Profile entity's actual getters/setters
            profile.setFullName(updatedProfile.getFullName());
            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
            profile.setGender(updatedProfile.getGender());
            profile.setReligion(updatedProfile.getReligion());
            profile.setCaste(updatedProfile.getCaste());
            profile.setSubCaste(updatedProfile.getSubCaste());
            profile.setMaritalStatus(updatedProfile.getMaritalStatus());
            profile.setHeightCm(updatedProfile.getHeightCm());
            profile.setComplexion(updatedProfile.getComplexion());
            profile.setBodyType(updatedProfile.getBodyType());
            profile.setEducation(updatedProfile.getEducation());
            profile.setOccupation(updatedProfile.getOccupation());
            profile.setAnnualIncome(updatedProfile.getAnnualIncome());
            profile.setCity(updatedProfile.getCity());
            profile.setState(updatedProfile.getState());
            profile.setCountry(updatedProfile.getCountry());
            profile.setAboutMe(updatedProfile.getAboutMe());
            profile.setPhotoUrl(updatedProfile.getPhotoUrl());
            profile.setActive(updatedProfile.isActive()); // For boolean is/set
            profile.setPreferredPartnerMinAge(updatedProfile.getPreferredPartnerMinAge());
            profile.setPreferredPartnerMaxAge(updatedProfile.getPreferredPartnerMaxAge());
            profile.setPreferredPartnerReligion(updatedProfile.getPreferredPartnerReligion());
            profile.setPreferredPartnerCaste(updatedProfile.getPreferredPartnerCaste());
            profile.setPreferredPartnerMinHeightCm(updatedProfile.getPreferredPartnerMinHeightCm());
            profile.setPreferredPartnerMaxHeightCm(updatedProfile.getPreferredPartnerMaxHeightCm());

            return profileRepository.save(profile);
        }).orElseThrow(() -> new RuntimeException("Profile not found with id " + profileId));
    }

    // --- Delete Profile ---
    public void deleteProfile(Long appUserId, Long profileId) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("Profile not found with id " + profileId));

        if (!profile.getAppUser().getId().equals(appUserId)) {
            throw new RuntimeException("Unauthorized: Profile does not belong to this user.");
        }
        profileRepository.delete(profile);
    }

    // --- Search Profiles ---
    // Adjust this method signature and implementation to use the new Profile fields
    // The controller's search call and this method's parameters must match.
    public List<Profile> searchProfiles(
        String gender,
        String religion,
        String caste,
        Integer minAge,
        Integer maxAge,
        Double minHeightCm, // Use heightCm
        Double maxHeightCm, // Use heightCm
        String maritalStatus, // Add maritalStatus if needed in search
        String education,
        String occupation,
        String city,
        String state,
        String country
    ) {
        // Implement your search logic here using profileRepository.findBySearchCriteria
        // Make sure findBySearchCriteria exists in ProfileRepository with this exact signature
        return profileRepository.findBySearchCriteria(
            gender, religion, caste, minAge, maxAge,
            minHeightCm, maxHeightCm, maritalStatus,
            education, occupation, city, state, country
        );
    }
}