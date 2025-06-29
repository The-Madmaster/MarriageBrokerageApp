package com.marriagebureau.usermanagement.service; // Corrected package name

import com.marriagebureau.entity.AppProfile; // Changed from Profile to AppProfile
import com.marriagebureau.entity.AppUser;    // Changed from User to AppUser
import com.marriagebureau.usermanagement.repository.ProfileRepository; // Corrected package and name
import com.marriagebureau.usermanagement.repository.AppUserRepository; // Changed from UserRepository to AppUserRepository
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository; // Changed from UserRepository to AppUserRepository

    @Autowired
    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) { // Changed parameter type
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository; // Corrected variable name
    }

    @Transactional
    public AppProfile createProfile(AppProfile profile, Long userId) { // Changed return type and parameter type
        // Ensure the AppUser exists before creating a profile linked to them
        AppUser appUser = appUserRepository.findById(userId) // Changed User to AppUser and userRepository to appUserRepository
                                         .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        profile.setCreatedByUser(appUser); // Link the profile to the appUser
        profile.setCreatedDate(LocalDateTime.now());
        profile.setLastUpdatedDate(LocalDateTime.now());
        profile.setActive(true); // Default to active

        // You might add more business logic here, e.g., validation
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<AppProfile> getProfileById(Long id) { // Changed return type
        return profileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AppProfile> getProfileByUserId(Long userId) { // Changed return type
        return profileRepository.findByCreatedByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public Optional<AppProfile> getProfileByEmail(String email) { // Changed return type
        return profileRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<AppProfile> getAllProfiles() { // Changed return type
        return profileRepository.findAll();
    }

    @Transactional
    public AppProfile updateProfile(Long id, AppProfile updatedProfileDetails) { // Changed return type and parameter type
        AppProfile existingProfile = profileRepository.findById(id) // Changed type
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + id));

        // Update fields individually to avoid overwriting with nulls unless intended
        existingProfile.setFullName(updatedProfileDetails.getFullName());
        existingProfile.setGender(updatedProfileDetails.getGender());
        existingProfile.setDateOfBirth(updatedProfileDetails.getDateOfBirth());
        existingProfile.setReligion(updatedProfileDetails.getReligion());
        existingProfile.setMaritalStatus(updatedProfileDetails.getMaritalStatus());
        existingProfile.setAboutMe(updatedProfileDetails.getAboutMe());
        existingProfile.setPhotoUrl(updatedProfileDetails.getPhotoUrl());
        existingProfile.setContactNumber(updatedProfileDetails.getContactNumber());
        existingProfile.setEmail(updatedProfileDetails.getEmail());
        existingProfile.setEducation(updatedProfileDetails.getEducation());
        existingProfile.setOccupation(updatedProfileDetails.getOccupation());
        existingProfile.setAnnualIncome(updatedProfileDetails.getAnnualIncome());
        existingProfile.setCity(updatedProfileDetails.getCity());
        existingProfile.setState(updatedProfileDetails.getState());
        existingProfile.setCountry(updatedProfileDetails.getCountry());
        existingProfile.setCaste(updatedProfileDetails.getCaste());
        existingProfile.setSubCaste(updatedProfileDetails.getSubCaste());
        existingProfile.setHeightCm(updatedProfileDetails.getHeightCm());
        existingProfile.setBodyType(updatedProfileDetails.getBodyType());
        existingProfile.setComplexion(updatedProfileDetails.getComplexion());
        existingProfile.setPreferredPartnerMinAge(updatedProfileDetails.getPreferredPartnerMinAge());
        existingProfile.setPreferredPartnerMaxAge(updatedProfileDetails.getPreferredPartnerMaxAge());
        existingProfile.setPreferredPartnerMinHeightCm(updatedProfileDetails.getPreferredPartnerMinHeightCm());
        existingProfile.setPreferredPartnerMaxHeightCm(updatedProfileDetails.getPreferredPartnerMaxHeightCm());
        existingProfile.setPreferredPartnerReligion(updatedProfileDetails.getPreferredPartnerReligion());
        existingProfile.setPreferredPartnerCaste(updatedProfileDetails.getPreferredPartnerCaste());

        existingProfile.setLastUpdatedDate(LocalDateTime.now());
        existingProfile.setActive(updatedProfileDetails.isActive()); // Allow updating active status

        return profileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new EntityNotFoundException("Profile not found with ID: " + id);
        }
        profileRepository.deleteById(id);
    }

    // You can add more business logic methods here as needed,
    // e.g., search, filter, match-making logic.
}