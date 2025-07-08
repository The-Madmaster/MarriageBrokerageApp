package com.marriagebureau.profile.service;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.profile.repository.ProfileRepository;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository; // Needed to link profile to user

    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Profile createProfile(Long userId, Profile profile) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // Check if a profile already exists for this user
        if (profileRepository.findByAppUserId(userId).isPresent()) { // Changed findByUserId to findByAppUserId
            throw new IllegalStateException("Profile already exists for user with ID: " + userId);
        }

        profile.setAppUser(user);
        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByAppUserId(userId); // Changed findByUserId to findByAppUserId
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional
    public Profile updateProfile(Long profileId, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        // Update fields individually to prevent overwriting user link or ID
        // Use the 'fullName' property instead of 'firstName' and 'lastName'
        if (updatedProfile.getFullName() != null) {
            existingProfile.setFullName(updatedProfile.getFullName());
        }
        if (updatedProfile.getDateOfBirth() != null) {
            existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        }
        if (updatedProfile.getGender() != null) {
            existingProfile.setGender(updatedProfile.getGender());
        }
        if (updatedProfile.getReligion() != null) {
            existingProfile.setReligion(updatedProfile.getReligion());
        }
        // Use 'caste' and 'subCaste' instead of 'community'
        if (updatedProfile.getCaste() != null) {
            existingProfile.setCaste(updatedProfile.getCaste());
        }
        if (updatedProfile.getSubCaste() != null) {
            existingProfile.setSubCaste(updatedProfile.getSubCaste());
        }
        if (updatedProfile.getMaritalStatus() != null) {
            existingProfile.setMaritalStatus(updatedProfile.getMaritalStatus());
        }
        if (updatedProfile.getHeightCm() != null) {
            existingProfile.setHeightCm(updatedProfile.getHeightCm());
        }
        if (updatedProfile.getComplexion() != null) {
            existingProfile.setComplexion(updatedProfile.getComplexion());
        }
        if (updatedProfile.getBodyType() != null) {
            existingProfile.setBodyType(updatedProfile.getBodyType());
        }
        if (updatedProfile.getEducation() != null) {
            existingProfile.setEducation(updatedProfile.getEducation());
        }
        if (updatedProfile.getOccupation() != null) {
            existingProfile.setOccupation(updatedProfile.getOccupation());
        }
        if (updatedProfile.getAnnualIncome() != null) {
            existingProfile.setAnnualIncome(updatedProfile.getAnnualIncome());
        }
        if (updatedProfile.getCity() != null) {
            existingProfile.setCity(updatedProfile.getCity());
        }
        if (updatedProfile.getState() != null) {
            existingProfile.setState(updatedProfile.getState());
        }
        if (updatedProfile.getCountry() != null) {
            existingProfile.setCountry(updatedProfile.getCountry());
        }
        if (updatedProfile.getAboutMe() != null) {
            existingProfile.setAboutMe(updatedProfile.getAboutMe());
        }
        if (updatedProfile.getPhotoUrl() != null) {
            existingProfile.setPhotoUrl(updatedProfile.getPhotoUrl());
        }
        // Preferred Partner Attributes
        if (updatedProfile.getPreferredPartnerMinAge() != null) {
            existingProfile.setPreferredPartnerMinAge(updatedProfile.getPreferredPartnerMinAge());
        }
        if (updatedProfile.getPreferredPartnerMaxAge() != null) {
            existingProfile.setPreferredPartnerMaxAge(updatedProfile.getPreferredPartnerMaxAge());
        }
        if (updatedProfile.getPreferredPartnerReligion() != null) {
            existingProfile.setPreferredPartnerReligion(updatedProfile.getPreferredPartnerReligion());
        }
        if (updatedProfile.getPreferredPartnerCaste() != null) {
            existingProfile.setPreferredPartnerCaste(updatedProfile.getPreferredPartnerCaste());
        }
        if (updatedProfile.getPreferredPartnerMinHeightCm() != null) {
            existingProfile.setPreferredPartnerMinHeightCm(updatedProfile.getPreferredPartnerMinHeightCm());
        }
        if (updatedProfile.getPreferredPartnerMaxHeightCm() != null) {
            existingProfile.setPreferredPartnerMaxHeightCm(updatedProfile.getPreferredPartnerMaxHeightCm());
        }

        return profileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new IllegalArgumentException("Profile not found with ID: " + id);
        }
        profileRepository.deleteById(id);
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}