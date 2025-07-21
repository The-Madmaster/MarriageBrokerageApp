package com.marriagebureau.profiles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // Ensure LocalDate is imported if used for dateOfBirth (though age is derived)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private Long id;
    private String email; // From AppUser
    private String fullName; // Direct field, no longer derived from first/last name
    private Integer age; // Derived from dateOfBirth in entity
    private String gender; // Enum name as String
    private String country;
    private String state;
    private String city;
    private String religion;
    private String caste;
    private String subCaste;
    private String educationLevel; // Mapped from 'education' in entity
    private String profession;     // Mapped from 'occupation' in entity
    private String maritalStatus; // Enum name as String
    private Integer heightCm;
    private String complexion;    // Enum name as String
    private String bodyType;      // Enum name as String
    private Double annualIncome;
    private String aboutMe;
    private String photoUrl;
    private boolean isActive;

    // Preferred Partner Criteria DTO fields
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion;
    private String preferredPartnerCaste;
    private Integer preferredPartnerMinHeightCm;
    private Integer preferredPartnerMaxHeightCm;
    private String motherTongue; // Enum name as String
    private String diet;         // Enum name as String
    private String smokingHabit; // Enum name as String
    private String drinkingHabit; // Enum name as String

    // Removed getFullName() method as fullName is now a direct field.
    // Removed firstName and lastName fields.
}