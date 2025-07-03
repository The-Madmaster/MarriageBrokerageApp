package com.marriagebureau.usermanagement.service;

import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.security.payload.ProfileSearchDto; // CORRECTED IMPORT
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.usermanagement.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Optional<Profile> createProfile(Profile profile, Long userId) {
        Optional<AppUser> existingUser = appUserRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = existingUser.get();

        if (profileRepository.findByAppUser_Id(userId).isPresent()) { // Corrected findByAppUserId to findByAppUser_Id
            return Optional.empty();
        }

        profile.setAppUser(appUser);
        Profile savedProfile = profileRepository.save(profile);
        return Optional.of(savedProfile);
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> getProfileByAppUserId(Long appUserId) {
        return profileRepository.findByAppUser_Id(appUserId); // Corrected findByAppUserId to findByAppUser_Id
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    public Optional<Profile> updateProfile(Long id, Profile updatedProfile) {
        return profileRepository.findById(id).map(existingProfile -> {
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
            existingProfile.setActive(updatedProfile.isActive());

            existingProfile.setPreferredPartnerMinAge(updatedProfile.getPreferredPartnerMinAge());
            existingProfile.setPreferredPartnerMaxAge(updatedProfile.getPreferredPartnerMaxAge());
            existingProfile.setPreferredPartnerReligion(updatedProfile.getPreferredPartnerReligion());
            existingProfile.setPreferredPartnerCaste(updatedProfile.getPreferredPartnerCaste());
            existingProfile.setPreferredPartnerMinHeightCm(updatedProfile.getPreferredPartnerMinHeightCm());
            existingProfile.setPreferredPartnerMaxHeightCm(updatedProfile.getPreferredPartnerMaxHeightCm());

            return profileRepository.save(existingProfile);
        }).or(Optional::empty);
    }

    @Transactional
    public boolean deleteProfile(Long id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Searches for profiles based on provided criteria.
     *
     * @param searchDto The DTO containing search parameters.
     * @return A list of profiles matching the criteria.
     */
    public List<Profile> searchProfiles(ProfileSearchDto searchDto) {
        return profileRepository.findBySearchCriteria( // Changed method name
                searchDto.getGender(),
                searchDto.getMinAge(),
                searchDto.getMaxAge(),
                searchDto.getReligion(),
                searchDto.getCaste(),
                searchDto.getMaritalStatus(),
                searchDto.getMinHeightCm(),
                searchDto.getMaxHeightCm(),
                searchDto.getCity(),
                searchDto.getState(),
                searchDto.getCountry(),
                searchDto.getEducation(),
                searchDto.getOccupation()
        );
    }
}