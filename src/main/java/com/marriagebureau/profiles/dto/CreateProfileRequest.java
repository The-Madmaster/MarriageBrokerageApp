package com.marriagebureau.profiles.dto;

import com.marriagebureau.usermanagement.entity.Profile; // To use Profile's enums
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CreateProfileRequest {

    // --- Core Personal Details ---
    @NotBlank(message = "Full name is mandatory")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of Birth must be in the past") // Ensure birthdate is in the past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // For consistent date format (YYYY-MM-DD)
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private Profile.Gender gender;

    @NotNull(message = "Marital Status is mandatory")
    private Profile.MaritalStatus maritalStatus;

    @NotNull(message = "Height in centimeters is mandatory")
    @Min(value = 50, message = "Height must be at least 50 cm") // Realistic min height
    @Max(value = 300, message = "Height cannot exceed 300 cm") // Realistic max height
    private Integer heightCm;

    // --- Cultural/Background Details ---
    @NotBlank(message = "Religion is mandatory")
    @Size(max = 50, message = "Religion cannot exceed 50 characters")
    private String religion;

    @NotBlank(message = "Caste is mandatory")
    @Size(max = 50, message = "Caste cannot exceed 50 characters")
    private String caste;

    @Size(max = 50, message = "Sub-Caste cannot exceed 50 characters")
    private String subCaste; // Optional

    @NotNull(message = "Mother Tongue is mandatory")
    private Profile.MotherTongue motherTongue;

    @NotBlank(message = "Country is mandatory")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "State is mandatory")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "City is mandatory")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    // --- Physical Attributes ---
    @NotNull(message = "Complexion is mandatory")
    private Profile.Complexion complexion;

    @NotNull(message = "Body Type is mandatory")
    private Profile.BodyType bodyType;

    // --- Professional/Educational Details ---
    @NotBlank(message = "Education is mandatory")
    @Size(max = 200, message = "Education cannot exceed 200 characters")
    private String education;

    @NotBlank(message = "Occupation is mandatory")
    @Size(max = 200, message = "Occupation cannot exceed 200 characters")
    private String occupation;

    @NotNull(message = "Annual Income is mandatory")
    @PositiveOrZero(message = "Annual Income must be a non-negative value")
    private Double annualIncome;

    // --- Lifestyle & Habits ---
    @NotNull(message = "Diet is mandatory")
    private Profile.Diet diet;

    @NotNull(message = "Smoking Habit is mandatory")
    private Profile.SmokingHabit smokingHabit;

    @NotNull(message = "Drinking Habit is mandatory")
    private Profile.DrinkingHabit drinkingHabit;

    // --- About Me & Photos ---
    @Size(max = 1000, message = "About Me cannot exceed 1000 characters")
    private String aboutMe; // Optional

    @URL(message = "Photo URL must be a valid URL")
    @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
    private String photoUrl; // Optional

    // --- Preferred Partner Criteria (all optional for creation) ---
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