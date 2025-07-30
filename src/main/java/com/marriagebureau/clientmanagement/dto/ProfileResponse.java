package com.marriagebureau.clientmanagement.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO for representing a client profile in API responses.
 * Includes all relevant details for display.
 */
@Data
@Builder
public class ProfileResponse {
    private Long id;
    private Long brokerId;
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
    private boolean isActive;

    // --- Preferred Partner Criteria ---
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion;
    private String preferredPartnerCaste;
    private Integer preferredPartnerMinHeightCm;
    private Integer preferredPartnerMaxHeightCm;
    
    // --- Auditing Fields ---
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}