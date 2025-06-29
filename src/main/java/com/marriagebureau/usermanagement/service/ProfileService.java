package com.marriagebureau.service;

import com.marriagebureau.entity.Profile;
import com.marriagebureau.entity.User;
import com.marriagebureau.repository.ProfileRepository;
import com.marriagebureau.usermanagement.repository.UserRepository; // Import UserRepository
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
    private final UserRepository userRepository; // Inject UserRepository to find User

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Profile createProfile(Profile profile, Long userId) {
        // Ensure the User exists before creating a profile linked to them
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        profile.setCreatedByUser(user); // Link the profile to the user
        profile.setCreatedDate(LocalDateTime.now());
        profile.setLastUpdatedDate(LocalDateTime.now());
        profile.setActive(true); // Default to active

        // You might add more business logic here, e.g., validation
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByCreatedByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    public Profile updateProfile(Long id, Profile updatedProfileDetails) {
        Profile existingProfile = profileRepository.findById(id)
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