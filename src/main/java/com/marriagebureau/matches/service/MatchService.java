// src/main/java/com/marriagebureau/matches/service/MatchService.java
package com.marriagebureau.matches.service;

import com.marriagebureau.matches.dto.MatchResultDto;
import com.marriagebureau.profiles.dto.ProfileResponse;
import com.marriagebureau.profiles.repository.ProfileRepository;
import com.marriagebureau.profiles.service.ProfileService; // To use convertToDto
import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final ProfileRepository profileRepository;
    private final ProfileService profileService; // Re-use ProfileService's convertToDto helper

    @Transactional(readOnly = true)
    public List<MatchResultDto> findMatchesForProfile(Long requestingProfileId) {
        Profile requestingProfile = profileRepository.findById(requestingProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Requesting Profile not found with ID: " + requestingProfileId));

        List<Profile> allOtherProfiles = profileRepository.findAllActiveProfilesExcluding(requestingProfileId);

        return allOtherProfiles.stream()
                .map(otherProfile -> calculateMatch(requestingProfile, otherProfile))
                .filter(matchResult -> matchResult.getCompatibilityScore() > 0) // Only return profiles that are a "match"
                .collect(Collectors.toList());
    }

    private MatchResultDto calculateMatch(Profile requestingProfile, Profile otherProfile) {
        // Initialize score. For now, it's either 0 (no match) or 1 (full match based on hard criteria)
        double score = 1.0; // Assume full match initially, then reduce if criteria not met

        // 1. Self-Exclusion (already handled by findAllActiveProfilesExcluding)
        // 2. Active Status (already handled by findAllActiveProfilesExcluding)

        // 3. Heterosexual Gender Compatibility (Hard Rule)
        boolean genderCompatible = (requestingProfile.getGender() == Profile.Gender.MALE && otherProfile.getGender() == Profile.Gender.FEMALE) ||
                                   (requestingProfile.getGender() == Profile.Gender.FEMALE && otherProfile.getGender() == Profile.Gender.MALE);
        if (!genderCompatible) {
            score = 0.0;
        }

        // 4. Age Range (Hard Rule)
        // Ensure requestingProfile.getPreferredPartnerMinAge() and MaxAge are not null before comparison
        if (score > 0 && (requestingProfile.getPreferredPartnerMinAge() != null && otherProfile.getAge() < requestingProfile.getPreferredPartnerMinAge())) {
            score = 0.0;
        }
        if (score > 0 && (requestingProfile.getPreferredPartnerMaxAge() != null && otherProfile.getAge() > requestingProfile.getPreferredPartnerMaxAge())) {
            score = 0.0;
        }


        // 5. Height Range (Hard Rule)
        if (score > 0 && (requestingProfile.getPreferredPartnerMinHeightCm() != null && otherProfile.getHeightCm() < requestingProfile.getPreferredPartnerMinHeightCm())) {
            score = 0.0;
        }
        if (score > 0 && (requestingProfile.getPreferredPartnerMaxHeightCm() != null && otherProfile.getHeightCm() > requestingProfile.getPreferredPartnerMaxHeightCm())) {
            score = 0.0;
        }


        // 6. Religion (Hard Rule)
        if (score > 0 && (requestingProfile.getPreferredPartnerReligion() != null &&
                          !requestingProfile.getPreferredPartnerReligion().equalsIgnoreCase(otherProfile.getReligion()))) {
            score = 0.0;
        }

        // 7. Caste (Hard Rule)
        if (score > 0 && (requestingProfile.getPreferredPartnerCaste() != null &&
                          !requestingProfile.getPreferredPartnerCaste().equalsIgnoreCase(otherProfile.getCaste()))) {
            score = 0.0;
        }

        // 8. Sub-Caste (Hard Rule)
        if (score > 0 && (requestingProfile.getPreferredPartnerSubCaste() != null &&
                          !requestingProfile.getPreferredPartnerSubCaste().equalsIgnoreCase(otherProfile.getSubCaste()))) {
            score = 0.0;
        }

        // 9. Location (City, State, Country) (Hard Rule)
        if (score > 0 && (requestingProfile.getPreferredPartnerCountry() != null &&
                          !requestingProfile.getPreferredPartnerCountry().equalsIgnoreCase(otherProfile.getCountry()))) {
            score = 0.0;
        }
        if (score > 0 && (requestingProfile.getPreferredPartnerState() != null &&
                          !requestingProfile.getPreferredPartnerState().equalsIgnoreCase(otherProfile.getState()))) {
            score = 0.0;
        }
        if (score > 0 && (requestingProfile.getPreferredPartnerCity() != null &&
                          !requestingProfile.getPreferredPartnerCity().equalsIgnoreCase(otherProfile.getCity()))) {
            score = 0.0;
        }

        // 10. Marital Status Compatibility (Hard Rule - Refined for Indian Context)
        if (score > 0) {
            boolean maritalStatusCompatible = false;
            if (requestingProfile.getMaritalStatus() == Profile.MaritalStatus.NEVER_MARRIED) {
                if (otherProfile.getMaritalStatus() == Profile.MaritalStatus.NEVER_MARRIED) {
                    maritalStatusCompatible = true;
                }
            } else if (requestingProfile.getMaritalStatus() == Profile.MaritalStatus.DIVORCED ||
                       requestingProfile.getMaritalStatus() == Profile.MaritalStatus.WIDOWED ||
                       requestingProfile.getMaritalStatus() == Profile.MaritalStatus.ANNULLED) {
                // Previously married individuals can only match with other previously married individuals
                if (otherProfile.getMaritalStatus() == Profile.MaritalStatus.DIVORCED ||
                    otherProfile.getMaritalStatus() == Profile.MaritalStatus.WIDOWED ||
                    otherProfile.getMaritalStatus() == Profile.MaritalStatus.ANNULLED) {
                    maritalStatusCompatible = true;
                }
            }
            if (!maritalStatusCompatible) {
                score = 0.0;
            }
        }

        // If score is still 1.0, it's a full match based on all hard criteria
        ProfileResponse matchedProfileResponse = profileService.convertToDto(otherProfile);

        return MatchResultDto.builder()
                .matchedProfile(matchedProfileResponse)
                .compatibilityScore(score)
                .build();
    }
}