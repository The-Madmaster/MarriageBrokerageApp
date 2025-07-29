package com.marriagebureau.profiles.dto;

import com.marriagebureau.usermanagement.entity.Profile; // To use Profile's enums
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
// import java.net.URL;
import org.hibernate.validator.constraints.URL;
import com.marriagebureau.profiles.repository.ProfileRepository;

@Data
public class UpdateProfileRequest {

    // --- Core Personal Details ---
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @Past(message = "Date of Birth must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private Profile.Gender gender;
    private Profile.MaritalStatus maritalStatus;

    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 300, message = "Height cannot exceed 300 cm")
    private Integer heightCm;

    // --- Cultural/Background Details ---
    @Size(max = 50, message = "Religion cannot exceed 50 characters")
    private String religion;

    @Size(max = 50, message = "Caste cannot exceed 50 characters")
    private String caste;

    @Size(max = 50, message = "Sub-Caste cannot exceed 50 characters")
    private String subCaste;

    private Profile.MotherTongue motherTongue;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    // --- Physical Attributes ---
    private Profile.Complexion complexion;
    private Profile.BodyType bodyType;

    // --- Professional/Educational Details ---
    @Size(max = 200, message = "Education cannot exceed 200 characters")
    private String education;

    @Size(max = 200, message = "Occupation cannot exceed 200 characters")
    private String occupation;

    @PositiveOrZero(message = "Annual Income must be a non-negative value")
    private Double annualIncome;

    // --- Lifestyle & Habits ---
    private Profile.Diet diet;
    private Profile.SmokingHabit smokingHabit;
    private Profile.DrinkingHabit drinkingHabit;

    // --- About Me & Photos ---
    @Size(max = 1000, message = "About Me cannot exceed 1000 characters")
    private String aboutMe;

    @URL(message = "Photo URL must be a valid URL")
    @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
    private String photoUrl;

    // --- Account Status ---
    private Boolean isActive; // Use Boolean for update, allowing null if not being updated

    // --- Preferred Partner Criteria ---
    @Min(value = 18, message = "Preferred minimum age must be at least 18")
    private Integer preferredPartnerMinAge;

    @Max(value = 99, message = "Preferred maximum age cannot exceed 99")
    private Integer preferredPartnerMaxAge;

    @Size(max = 50, message = "Preferred partner religion cannot exceed 50 characters")
    private String preferredPartnerReligion;

    @Size(max = 50, message = "Preferred partner caste cannot exceed 50 characters")
    private String preferredPartnerCaste;

    // START: ADDED NEW FIELDS FOR PREFERRED PARTNER CRITERIA
    @Size(max = 50, message = "Preferred partner sub-caste cannot exceed 50 characters")
    private String preferredPartnerSubCaste;

    @Size(max = 100, message = "Preferred partner city cannot exceed 100 characters")
    private String preferredPartnerCity;

    @Size(max = 100, message = "Preferred partner country cannot exceed 100 characters")
    private String preferredPartnerCountry;

    @Size(max = 100, message = "Preferred partner state cannot exceed 100 characters")
    private String preferredPartnerState;
    // END: ADDED NEW FIELDS FOR PREFERRED PARTNER CRITERIA

    @Min(value = 50, message = "Preferred minimum height must be at least 50 cm")
    private Integer preferredPartnerMinHeightCm;

    @Max(value = 300, message = "Preferred maximum height cannot exceed 300 cm")
    private Integer preferredPartnerMaxHeightCm;
}