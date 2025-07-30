package com.marriagebureau.clientmanagement.mapper;

import com.marriagebureau.clientmanagement.dto.ProfileResponse;
import com.marriagebureau.clientmanagement.model.Profile;

public class ProfileMapper {

    public static ProfileResponse toProfileResponse(Profile profile) {
        if (profile == null) {
            return null;
        }
        return ProfileResponse.builder()
                .id(profile.getId())
                .brokerId(profile.getBroker().getId())
                .email(profile.getBroker().getEmail())
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
                .preferredPartnerMinAge(profile.getPreferredPartnerMinAge())
                .preferredPartnerMaxAge(profile.getPreferredPartnerMaxAge())
                .preferredPartnerReligion(profile.getPreferredPartnerReligion())
                .preferredPartnerCaste(profile.getPreferredPartnerCaste())
                .preferredPartnerMinHeightCm(profile.getPreferredPartnerMinHeightCm())
                .preferredPartnerMaxHeightCm(profile.getPreferredPartnerMaxHeightCm())
                .createdDate(profile.getCreatedDate())
                .lastUpdatedDate(profile.getLastUpdatedDate())
                .build();
    }
}