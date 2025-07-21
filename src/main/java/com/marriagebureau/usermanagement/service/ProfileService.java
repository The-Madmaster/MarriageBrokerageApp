// src/main/java/com/marriagebureau/usermanagement/service/ProfileService.java
package com.marriagebureau.usermanagement.service;

import com.marriagebureau.profiles.dto.ProfileDTO; // Assuming this DTO exists for search results
import com.marriagebureau.profiles.dto.ProfileSearchRequest; // Assuming this DTO exists for search parameters
import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.ProfileRepository;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl; // Import PageImpl
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Import for stream operations

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    public ProfileService(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    // --- Create Profile ---
    @Transactional
    public Profile createProfile(Long appUserId, Profile profile) {
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("AppUser not found with id " + appUserId));

        // Ensure AppUser does not already have a profile
        if (appUser.getProfile() != null) {
            throw new RuntimeException("Profile already exists for AppUser with ID: " + appUserId);
        }

        profile.setAppUser(appUser); // Link the Profile to the AppUser object
        Profile savedProfile = profileRepository.save(profile);
        appUser.setProfile(savedProfile); // Establish bidirectional link
        appUserRepository.save(appUser); // Save AppUser to persist the link
        return savedProfile;
    }

    // --- Get All Profiles (UPDATED to return Page<ProfileDTO>) ---
    public Page<ProfileDTO> getAllProfiles(Pageable pageable) {
        Page<Profile> profilesPage = profileRepository.findAll(pageable);
        List<ProfileDTO> profileDTOs = profilesPage.getContent().stream()
                .map(this::convertToProfileDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(profileDTOs, pageable, profilesPage.getTotalElements());
    }

    // --- Get Profile by ID ---
    public Optional<Profile> getProfileById(Long profileId) {
        return profileRepository.findById(profileId);
    }

    // --- NEW: Get Comprehensive Profile Data for PDF Generation --- ⭐
    public Profile getBiodataForPdf(Long profileId) {
        // Fetch the profile. If not found, throw an exception.
        // You might want to fetch with joins if specific eager fetching is needed for PDF data.
        // For simplicity, this assumes your Profile entity already contains all necessary fields
        // or has LAZY loading configured to fetch related AppUser data when accessed.
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id " + profileId));
    }


    // --- Update Profile ---
    @Transactional
    public Profile updateProfile(Long appUserId, Long profileId, Profile updatedProfile) {
        return profileRepository.findById(profileId).map(profile -> {
            // Ensure the profile belongs to the authenticated appUser
            if (!profile.getAppUser().getId().equals(appUserId)) {
                throw new RuntimeException("Unauthorized: Profile does not belong to this user.");
            }

            // Update fullName directly from the updatedProfile object
            profile.setFullName(updatedProfile.getFullName()); // Use fullName directly

            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
            profile.setGender(updatedProfile.getGender()); // Now an enum
            profile.setReligion(updatedProfile.getReligion());
            profile.setCaste(updatedProfile.getCaste());
            profile.setSubCaste(updatedProfile.getSubCaste());
            profile.setMaritalStatus(updatedProfile.getMaritalStatus()); // Now an enum
            profile.setHeightCm(updatedProfile.getHeightCm());
            profile.setComplexion(updatedProfile.getComplexion()); // Now an enum
            profile.setBodyType(updatedProfile.getBodyType()); // Now an enum
            profile.setEducation(updatedProfile.getEducation());
            profile.setOccupation(updatedProfile.getOccupation());
            profile.setAnnualIncome(updatedProfile.getAnnualIncome());
            profile.setCity(updatedProfile.getCity());
            profile.setState(updatedProfile.getState());
            profile.setCountry(updatedProfile.getCountry());
            profile.setAboutMe(updatedProfile.getAboutMe());
            profile.setPhotoUrl(updatedProfile.getPhotoUrl());
            profile.setActive(updatedProfile.isActive());
            profile.setPreferredPartnerMinAge(updatedProfile.getPreferredPartnerMinAge());
            profile.setPreferredPartnerMaxAge(updatedProfile.getPreferredPartnerMaxAge());
            profile.setPreferredPartnerReligion(updatedProfile.getPreferredPartnerReligion());
            profile.setPreferredPartnerCaste(updatedProfile.getPreferredPartnerCaste());
            profile.setPreferredPartnerMinHeightCm(updatedProfile.getPreferredPartnerMinHeightCm());
            profile.setPreferredPartnerMaxHeightCm(updatedProfile.getPreferredPartnerMaxHeightCm());
            profile.setMotherTongue(updatedProfile.getMotherTongue()); // Now an enum
            profile.setDiet(updatedProfile.getDiet()); // Now an enum
            profile.setSmokingHabit(updatedProfile.getSmokingHabit()); // Now an enum
            profile.setDrinkingHabit(updatedProfile.getDrinkingHabit()); // Now an enum

            return profileRepository.save(profile);
        }).orElseThrow(() -> new RuntimeException("Profile not found with id " + profileId));
    }

    // --- Delete Profile ---
    @Transactional
    public void deleteProfile(Long appUserId, Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id " + profileId));

        if (!profile.getAppUser().getId().equals(appUserId)) {
            throw new RuntimeException("Unauthorized: Profile does not belong to this user.");
        }

        // It's crucial to break the bidirectional link before deleting the child entity.
        // This avoids potential foreign key constraint issues or unexpected behavior.
        AppUser appUser = profile.getAppUser();
        if (appUser != null) {
            appUser.setProfile(null); // Remove the reference from AppUser
            appUserRepository.save(appUser); // Save AppUser to update the relationship in DB
        }

        profileRepository.delete(profile);
    }

    // --- Search Profiles (UPDATED to use ProfileSearchRequest and return Page<ProfileDTO>) ---
    public Page<ProfileDTO> searchProfiles(ProfileSearchRequest searchRequest) {
        // Prepare pagination and sorting
        Sort.Direction direction = Sort.Direction.fromString(searchRequest.getSortDirection().toUpperCase());
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), direction, searchRequest.getSortBy());

        // Build dynamic query using Spring Data JPA Specification
        Specification<Profile> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Age Range (calculated from dateOfBirth)
            if (searchRequest.getMinAge() != null) {
                LocalDate minBirthDateAllowed = LocalDate.now().minusYears(searchRequest.getMinAge());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"), minBirthDateAllowed));
            }
            if (searchRequest.getMaxAge() != null) {
                LocalDate maxBirthDateAllowed = LocalDate.now().minusYears(searchRequest.getMaxAge() + 1).plusDays(1);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), maxBirthDateAllowed));
            }

            // Gender (using enum)
            if (searchRequest.getGender() != null && !searchRequest.getGender().isEmpty()) {
                try {
                    Profile.Gender genderEnum = Profile.Gender.valueOf(searchRequest.getGender().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("gender"), genderEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid gender value for search: " + searchRequest.getGender());
                }
            }

            // Location
            if (searchRequest.getCountry() != null && !searchRequest.getCountry().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("country"), searchRequest.getCountry()));
            }
            if (searchRequest.getState() != null && !searchRequest.getState().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("state"), searchRequest.getState()));
            }
            if (searchRequest.getCity() != null && !searchRequest.getCity().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("city"), searchRequest.getCity()));
            }

            // Religion & Caste
            if (searchRequest.getReligion() != null && !searchRequest.getReligion().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("religion"), searchRequest.getReligion()));
            }
            if (searchRequest.getCaste() != null && !searchRequest.getCaste().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("caste"), searchRequest.getCaste()));
            }
            if (searchRequest.getSubCaste() != null && !searchRequest.getSubCaste().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("subCaste"), searchRequest.getSubCaste()));
            }

            // Education & Profession
            if (searchRequest.getEducationLevel() != null && !searchRequest.getEducationLevel().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("education"), searchRequest.getEducationLevel()));
            }
            if (searchRequest.getProfession() != null && !searchRequest.getProfession().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("occupation"), searchRequest.getProfession()));
            }

            // Marital Status (using enum)
            if (searchRequest.getMaritalStatus() != null && !searchRequest.getMaritalStatus().isEmpty()) {
                try {
                    Profile.MaritalStatus maritalStatusEnum = Profile.MaritalStatus.valueOf(searchRequest.getMaritalStatus().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("maritalStatus"), maritalStatusEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid marital status value for search: " + searchRequest.getMaritalStatus());
                }
            }

            // Height Range
            if (searchRequest.getMinHeightCm() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("heightCm"), searchRequest.getMinHeightCm()));
            }
            if (searchRequest.getMaxHeightCm() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("heightCm"), searchRequest.getMaxHeightCm()));
            }

            // Mother Tongue (using enum)
            if (searchRequest.getMotherTongue() != null && !searchRequest.getMotherTongue().isEmpty()) {
                try {
                    Profile.MotherTongue motherTongueEnum = Profile.MotherTongue.valueOf(searchRequest.getMotherTongue().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("motherTongue"), motherTongueEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid mother tongue value for search: " + searchRequest.getMotherTongue());
                }
            }

            // Diet (using enum)
            if (searchRequest.getDiet() != null && !searchRequest.getDiet().isEmpty()) {
                try {
                    Profile.Diet dietEnum = Profile.Diet.valueOf(searchRequest.getDiet().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("diet"), dietEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid diet value for search: " + searchRequest.getDiet());
                }
            }

            // Smoking Habit (using enum)
            if (searchRequest.getSmokingHabit() != null && !searchRequest.getSmokingHabit().isEmpty()) {
                try {
                    Profile.SmokingHabit smokingHabitEnum = Profile.SmokingHabit.valueOf(searchRequest.getSmokingHabit().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("smokingHabit"), smokingHabitEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid smoking habit value for search: " + searchRequest.getSmokingHabit());
                }
            }

            // Drinking Habit (using enum)
            if (searchRequest.getDrinkingHabit() != null && !searchRequest.getDrinkingHabit().isEmpty()) {
                try {
                    Profile.DrinkingHabit drinkingHabitEnum = Profile.DrinkingHabit.valueOf(searchRequest.getDrinkingHabit().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("drinkingHabit"), drinkingHabitEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid drinking habit value for search: " + searchRequest.getDrinkingHabit());
                }
            }

            // Only search for active profiles
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Execute search and map to DTOs
        Page<Profile> profilePage = profileRepository.findAll(spec, pageable);

        return profilePage.map(this::convertToProfileDTO);
    }

    // Helper method to convert Profile entity to ProfileDTO
    private ProfileDTO convertToProfileDTO(Profile profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .email(profile.getAppUser() != null ? profile.getAppUser().getEmail() : null)
                .fullName(profile.getFullName()) // Use fullName directly
                .gender(profile.getGender() != null ? profile.getGender().name() : null)
                .age(profile.getAge()) // Calculated from dateOfBirth
                .country(profile.getCountry())
                .state(profile.getState())
                .city(profile.getCity())
                .religion(profile.getReligion())
                .caste(profile.getCaste())
                .subCaste(profile.getSubCaste())
                .educationLevel(profile.getEducation())
                .profession(profile.getOccupation())
                .maritalStatus(profile.getMaritalStatus() != null ? profile.getMaritalStatus().name() : null)
                .heightCm(profile.getHeightCm())
                .complexion(profile.getComplexion() != null ? profile.getComplexion().name() : null)
                .bodyType(profile.getBodyType() != null ? profile.getBodyType().name() : null)
                .annualIncome(profile.getAnnualIncome())
                .aboutMe(profile.getAboutMe())
                .photoUrl(profile.getPhotoUrl())
                .isActive(profile.isActive())
                .preferredPartnerMinAge(profile.getPreferredPartnerMinAge())
                .preferredPartnerMaxAge(profile.getPreferredPartnerMaxAge())
                .preferredPartnerReligion(profile.getPreferredPartnerReligion())
                .preferredPartnerCaste(profile.getPreferredPartnerCaste())
                .preferredPartnerMinHeightCm(profile.getPreferredPartnerMinHeightCm())
                .preferredPartnerMaxHeightCm(profile.getPreferredPartnerMaxHeightCm())
                .motherTongue(profile.getMotherTongue() != null ? profile.getMotherTongue().name() : null)
                .diet(profile.getDiet() != null ? profile.getDiet().name() : null)
                .smokingHabit(profile.getSmokingHabit() != null ? profile.getSmokingHabit().name() : null)
                .drinkingHabit(profile.getDrinkingHabit() != null ? profile.getDrinkingHabit().name() : null)
                .build();
    }
}