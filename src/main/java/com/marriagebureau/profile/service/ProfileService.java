// src/main/java/com/marriagebureau/profile/service/ProfileService.java

package com.marriagebureau.profile.service;

import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.entity.Profile; // Corrected import for Profile
import com.marriagebureau.usermanagement.repository.AppUserRepository; // Assuming you have an AppUserRepository
import com.marriagebureau.profile.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository; // Inject AppUserRepository to link AppUser with Profile

    @Autowired
    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Profile createProfile(Profile profile, Long appUserId) {
        // Check if an AppUser with the given ID exists
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found with ID: " + appUserId));

        // Check if a profile already exists for this user
        if (profileRepository.findByAppUserId(appUserId).isPresent()) {
            throw new IllegalStateException("Profile already exists for AppUser with ID: " + appUserId);
        }

        profile.setAppUser(appUser); // Link the profile to the AppUser
        profile.setCreatedDate(LocalDateTime.now());
        profile.setLastUpdatedDate(LocalDateTime.now());
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfileByAppUserId(Long appUserId) {
        return profileRepository.findByAppUserId(appUserId);
    }

    @Transactional
    public Profile updateProfile(Long id, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + id));

        // Update fields - copy properties from updatedProfile to existingProfile
        // Only update fields that are explicitly provided or allowed to be updated
        existingProfile.setFullName(updatedProfile.getFullName());
        existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        existingProfile.setGender(updatedProfile.getGender());
        existingProfile.setReligion(updatedProfile.getReligion());
        existingProfile.setCaste(updatedProfile.getCaste());
        existingProfile.setSubCaste(updatedProfile.getSubCaste());
        existingProfile.setMaritalStatus(updatedProfile.getMaritalStatus());
        existingProfile.setHeightCm(updatedProfile.getHeightCm());
        existingProfile.setComplexion(updatedProfile.getComplexion());
        existingProfile.setBodyType(updatedProfile.getBodyType());
        existingProfile.setEducation(updatedProfile.getEducation());
        existingProfile.setOccupation(updatedProfile.getOccupation());
        existingProfile.setAnnualIncome(updatedProfile.getAnnualIncome());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setAboutMe(updatedProfile.getAboutMe());
        existingProfile.setPhotoUrl(updatedProfile.getPhotoUrl());
        existingProfile.setActive(updatedProfile.isActive()); // isActive field

        // Update preferred partner attributes
        existingProfile.setPreferredPartnerMinAge(updatedProfile.getPreferredPartnerMinAge());
        existingProfile.setPreferredPartnerMaxAge(updatedProfile.getPreferredPartnerMaxAge());
        existingProfile.setPreferredPartnerReligion(updatedProfile.getPreferredPartnerReligion());
        existingProfile.setPreferredPartnerCaste(updatedProfile.getPreferredPartnerCaste());
        existingProfile.setPreferredPartnerMinHeightCm(updatedProfile.getPreferredPartnerMinHeightCm());
        existingProfile.setPreferredPartnerMaxHeightCm(updatedProfile.getPreferredPartnerMaxHeightCm());


        existingProfile.setLastUpdatedDate(LocalDateTime.now()); // Handled by @PreUpdate, but explicit setting is fine
        return profileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new EntityNotFoundException("Profile not found with ID: " + id);
        }
        profileRepository.deleteById(id);
    }
}