package com.marriagebureau.profiles.service;

import com.marriagebureau.profiles.dto.CreateProfileRequest;
import com.marriagebureau.profiles.dto.ProfileResponse;
import com.marriagebureau.profiles.dto.ProfileSearchRequest;
import com.marriagebureau.profiles.repository.ProfileRepository;
import com.marriagebureau.profiles.specification.ProfileSpecification;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import com.marriagebureau.usermanagement.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository; // Inject AppUserRepository to link AppUser to Profile

    @Transactional
    public ProfileResponse createProfile(Long appUserId, CreateProfileRequest request) {
        // Check if a profile already exists for this appUser
        if (profileRepository.findByAppUserId(appUserId).isPresent()) {
            throw new IllegalArgumentException("Profile already exists for AppUser ID: " + appUserId);
        }

        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with ID: " + appUserId));

        Profile profile = Profile.builder()
                .appUser(appUser)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .heightCm(request.getHeightCm())
                .religion(request.getReligion())
                .caste(request.getCaste())
                .subCaste(request.getSubCaste())
                .motherTongue(request.getMotherTongue())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .complexion(request.getComplexion())
                .bodyType(request.getBodyType())
                .education(request.getEducation())
                .occupation(request.getOccupation())
                .annualIncome(request.getAnnualIncome())
                .diet(request.getDiet())
                .smokingHabit(request.getSmokingHabit())
                .drinkingHabit(request.getDrinkingHabit())
                .aboutMe(request.getAboutMe())
                .photoUrl(request.getPhotoUrl())
                .isActive(true) // Default to active on creation
                // Preferred Partner Criteria
                .preferredPartnerMinAge(request.getPreferredPartnerMinAge())
                .preferredPartnerMaxAge(request.getPreferredPartnerMaxAge())
                .preferredPartnerReligion(request.getPreferredPartnerReligion())
                .preferredPartnerCaste(request.getPreferredPartnerCaste())
                // START: ADDED NEW FIELDS MAPPING FOR CREATE
                .preferredPartnerSubCaste(request.getPreferredPartnerSubCaste())
                .preferredPartnerCity(request.getPreferredPartnerCity())
                .preferredPartnerState(request.getPreferredPartnerState())
                .preferredPartnerCountry(request.getPreferredPartnerCountry())
                // END: ADDED NEW FIELDS MAPPING FOR CREATE
                .preferredPartnerMinHeightCm(request.getPreferredPartnerMinHeightCm())
                .preferredPartnerMaxHeightCm(request.getPreferredPartnerMaxHeightCm())
                .build();

        Profile savedProfile = profileRepository.save(profile);
        return convertToDto(savedProfile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));
        return convertToDto(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByAppUserId(Long appUserId) {
        Profile profile = profileRepository.findByAppUserId(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for AppUser ID: " + appUserId));
        return convertToDto(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(Long profileId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        // Update fields only if they are provided in the request
        Optional.ofNullable(request.getFullName()).ifPresent(profile::setFullName);
        Optional.ofNullable(request.getDateOfBirth()).ifPresent(profile::setDateOfBirth);
        Optional.ofNullable(request.getGender()).ifPresent(profile::setGender);
        Optional.ofNullable(request.getMaritalStatus()).ifPresent(profile::setMaritalStatus);
        Optional.ofNullable(request.getHeightCm()).ifPresent(profile::setHeightCm);
        Optional.ofNullable(request.getReligion()).ifPresent(profile::setReligion);
        Optional.ofNullable(request.getCaste()).ifPresent(profile::setCaste);
        Optional.ofNullable(request.getSubCaste()).ifPresent(profile::setSubCaste);
        Optional.ofNullable(request.getMotherTongue()).ifPresent(profile::setMotherTongue);
        Optional.ofNullable(request.getCountry()).ifPresent(profile::setCountry);
        Optional.ofNullable(request.getState()).ifPresent(profile::setState);
        Optional.ofNullable(request.getCity()).ifPresent(profile::setCity);
        Optional.ofNullable(request.getComplexion()).ifPresent(profile::setComplexion);
        Optional.ofNullable(request.getBodyType()).ifPresent(profile::setBodyType);
        Optional.ofNullable(request.getEducation()).ifPresent(profile::setEducation);
        Optional.ofNullable(request.getOccupation()).ifPresent(profile::setOccupation);
        Optional.ofNullable(request.getAnnualIncome()).ifPresent(profile::setAnnualIncome);
        Optional.ofNullable(request.getDiet()).ifPresent(profile::setDiet);
        Optional.ofNullable(request.getSmokingHabit()).ifPresent(profile::setSmokingHabit);
        Optional.ofNullable(request.getDrinkingHabit()).ifPresent(profile::setDrinkingHabit);
        Optional.ofNullable(request.getAboutMe()).ifPresent(profile::setAboutMe);
        Optional.ofNullable(request.getPhotoUrl()).ifPresent(profile::setPhotoUrl);
        Optional.ofNullable(request.getIsActive()).ifPresent(profile::setActive);

        // Update Preferred Partner Criteria
        Optional.ofNullable(request.getPreferredPartnerMinAge()).ifPresent(profile::setPreferredPartnerMinAge);
        Optional.ofNullable(request.getPreferredPartnerMaxAge()).ifPresent(profile::setPreferredPartnerMaxAge);
        Optional.ofNullable(request.getPreferredPartnerReligion()).ifPresent(profile::setPreferredPartnerReligion);
        Optional.ofNullable(request.getPreferredPartnerCaste()).ifPresent(profile::setPreferredPartnerCaste);
        // START: ADDED NEW FIELDS MAPPING FOR UPDATE
        Optional.ofNullable(request.getPreferredPartnerSubCaste()).ifPresent(profile::setPreferredPartnerSubCaste);
        Optional.ofNullable(request.getPreferredPartnerCity()).ifPresent(profile::setPreferredPartnerCity);
        Optional.ofNullable(request.getPreferredPartnerState()).ifPresent(profile::setPreferredPartnerState);
        Optional.ofNullable(request.getPreferredPartnerCountry()).ifPresent(profile::setPreferredPartnerCountry);
        // END: ADDED NEW FIELDS MAPPING FOR UPDATE
        Optional.ofNullable(request.getPreferredPartnerMinHeightCm()).ifPresent(profile::setPreferredPartnerMinHeightCm);
        Optional.ofNullable(request.getPreferredPartnerMaxHeightCm()).ifPresent(profile::setPreferredPartnerMaxHeightCm);

        Profile updatedProfile = profileRepository.save(profile);
        return convertToDto(updatedProfile);
    }

    @Transactional
    public void deleteProfile(Long profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Profile not found with ID: " + profileId);
        }
        profileRepository.deleteById(profileId);
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponse> searchProfiles(ProfileSearchRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        ProfileSpecification spec = new ProfileSpecification(request);

        Page<Profile> profilesPage = profileRepository.findAll(spec, pageable);
        return profilesPage.map(this::convertToDto);
    }

    public String getBiodataForPdf(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        StringBuilder biodata = new StringBuilder();
        biodata.append("--- BIODATA ---\n");
        biodata.append("Full Name: ").append(profile.getFullName()).append("\n");
        biodata.append("Age: ").append(profile.getAge()).append("\n");
        biodata.append("Gender: ").append(profile.getGender()).append("\n");
        biodata.append("Marital Status: ").append(profile.getMaritalStatus()).append("\n");
        biodata.append("Religion: ").append(profile.getReligion()).append("\n");
        biodata.append("Caste: ").append(profile.getCaste()).append("\n");
        biodata.append("Sub-Caste: ").append(profile.getSubCaste()).append("\n");
        biodata.append("City: ").append(profile.getCity()).append(", ").append(profile.getState()).append(", ").append(profile.getCountry()).append("\n");
        biodata.append("Education: ").append(profile.getEducation()).append("\n");
        biodata.append("Occupation: ").append(profile.getOccupation()).append("\n");
        biodata.append("Annual Income: ").append(profile.getAnnualIncome()).append("\n");
        biodata.append("About Me: ").append(profile.getAboutMe() != null ? profile.getAboutMe() : "N/A").append("\n");

        return biodata.toString();
    }

    // Helper method to convert Profile entity to ProfileResponse DTO
    private ProfileResponse convertToDto(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .appUserId(profile.getAppUser().getId())
                .email(profile.getAppUser().getEmail())
                .fullName(profile.getFullName())
                .age(profile.getAge())
                .gender(profile.getGender() != null ? profile.getGender().name() : null)
                .maritalStatus(profile.getMaritalStatus() != null ? profile.getMaritalStatus().name() : null)
                .heightCm(profile.getHeightCm())
                .religion(profile.getReligion())
                .caste(profile.getCaste())
                .subCaste(profile.getSubCaste())
                .motherTongue(profile.getMotherTongue() != null ? profile.getMotherTongue().name() : null)
                .country(profile.getCountry())
                .state(profile.getState())
                .city(profile.getCity())
                .complexion(profile.getComplexion() != null ? profile.getComplexion().name() : null)
                .bodyType(profile.getBodyType() != null ? profile.getBodyType().name() : null)
                .education(profile.getEducation())
                .occupation(profile.getOccupation())
                .annualIncome(profile.getAnnualIncome())
                .diet(profile.getDiet() != null ? profile.getDiet().name() : null)
                .smokingHabit(profile.getSmokingHabit() != null ? profile.getSmokingHabit().name() : null)
                .drinkingHabit(profile.getDrinkingHabit() != null ? profile.getDrinkingHabit().name() : null)
                .aboutMe(profile.getAboutMe())
                .photoUrl(profile.getPhotoUrl())
                .isActive(profile.isActive())
                // Preferred Partner Criteria
                .preferredPartnerMinAge(profile.getPreferredPartnerMinAge())
                .preferredPartnerMaxAge(profile.getPreferredPartnerMaxAge())
                .preferredPartnerReligion(profile.getPreferredPartnerReligion())
                .preferredPartnerCaste(profile.getPreferredPartnerCaste())
                // START: ADDED NEW FIELDS MAPPING FOR DTO CONVERSION
                .preferredPartnerSubCaste(profile.getPreferredPartnerSubCaste())
                .preferredPartnerCity(profile.getPreferredPartnerCity())
                .preferredPartnerState(profile.getPreferredPartnerState())
                .preferredPartnerCountry(profile.getPreferredPartnerCountry())
                // END: ADDED NEW FIELDS MAPPING FOR DTO CONVERSION
                .preferredPartnerMinHeightCm(profile.getPreferredPartnerMinHeightCm())
                .preferredPartnerMaxHeightCm(profile.getPreferredPartnerMaxHeightCm())
                .createdDate(profile.getCreatedDate())
                .lastUpdatedDate(profile.getLastUpdatedDate())
                .build();
    }
}