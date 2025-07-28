package com.marriagebureau.profiles.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProfileResponse {
    private Long id;
    private Long appUserId;
    private String email;

    // --- Core Personal Details ---
    private String fullName;
    private Integer age;
    private String gender;
    private String maritalStatus;
    private Integer heightCm;

    // --- Cultural/Background Details ---
    private String religion;
    private String caste;
    private String subCaste;
    private String motherTongue;
    private String country;
    private String state;
    private String city;

    // --- Physical Attributes ---
    private String complexion;
    private String bodyType;

    // --- Professional/Educational Details ---
    private String education;
    private String occupation;
    private Double annualIncome;

    // --- Lifestyle & Habits ---
    private String diet;
    private String smokingHabit;
    private String drinkingHabit;

    // --- About Me & Photos ---
    private String aboutMe;
    private String photoUrl;

    // --- Account Status ---
    private boolean isActive;

    // --- Preferred Partner Criteria ---
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion;
    private String preferredPartnerCaste;
    // START: ADDED NEW FIELDS FOR PREFERRED PARTNER CRITERIA
    private String preferredPartnerSubCaste;
    private String preferredPartnerCity;
    private String preferredPartnerState;
    private String preferredPartnerCountry;
    // END: ADDED NEW FIELDS FOR PREFERRED PARTNER CRITERIA
    private Integer preferredPartnerMinHeightCm;
    private Integer preferredPartnerMaxHeightCm;

    // --- Auditing Fields (if you add them to your Profile entity) ---
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}