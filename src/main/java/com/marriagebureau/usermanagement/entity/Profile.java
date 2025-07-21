// src/main/java/com/marriagebureau/usermanagement/entity/Profile.java
package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder; // Added @Builder for easier object creation
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period; // For calculating age

@Entity
@Table(name = "profiles") // Table for user profiles
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Provides a builder pattern for creating instances
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", unique = true, nullable = false)
    private AppUser appUser; // Link to the AppUser

    // --- Core Personal Details ---
    @Column(name = "full_name", nullable = false) // Re-added nullable=false if fullName is mandatory
    private String fullName;
    private LocalDate dateOfBirth; // Use LocalDate for birthdate

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private Gender gender;

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private MaritalStatus maritalStatus;

    private Integer heightCm; // Height in centimeters

    // --- Cultural/Background Details ---
    private String religion;
    private String caste;
    private String subCaste;

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private MotherTongue motherTongue; // Using an enum for common mother tongues

    private String country;
    private String state;
    private String city;

    // --- Physical Attributes ---
    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private Complexion complexion; // e.g., "FAIR", "WHEATISH", "DARK"

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private BodyType bodyType;    // e.g., "SLIM", "ATHLETIC", "AVERAGE", "HEAVY"

    // --- Professional/Educational Details ---
    private String education; // General education level (e.g., "Bachelors", "Masters")
    private String occupation;
    private Double annualIncome; // Could be String for flexibility (e.g., "5-10 lakhs") or numeric

    // --- Lifestyle & Habits ---
    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private Diet diet;

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private SmokingHabit smokingHabit;

    @Enumerated(EnumType.STRING) // Store enum names as strings in DB
    private DrinkingHabit drinkingHabit;

    // --- About Me & Photos ---
    @Column(length = 1000) // Longer text field
    private String aboutMe;
    private String photoUrl; // URL to profile photo

    // --- Account Status ---
    @Builder.Default // Lombok's way to provide a default value if not explicitly set
    private boolean isActive = true; // For user deactivation/activation

    // --- Preferred Partner Criteria (Optional - consider making this a separate entity/DTO if complex) ---
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion; // Could be enum, depending on how many options
    private String preferredPartnerCaste;    // Could be enum, depending on how many options
    private Integer preferredPartnerMinHeightCm;
    private Integer preferredPartnerMaxHeightCm;
    // Add more preferred partner criteria as needed (e.g., education, location)


    // --- Enums for Profile fields ---

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum MaritalStatus {
        NEVER_MARRIED,
        DIVORCED,
        WIDOWED,
        ANNULLED // For annulled marriage
    }

    public enum Complexion {
        VERY_FAIR,
        FAIR,
        WHEATISH,
        MEDIUM,
        DARK
    }

    public enum BodyType {
        SLIM,
        ATHLETIC,
        AVERAGE,
        HEAVY
    }

    public enum Diet {
        VEGETARIAN,
        NON_VEGETARIAN,
        VEGAN,
        EGGETARIAN // For those who eat eggs but no meat
    }

    public enum SmokingHabit {
        NON_SMOKER,
        OCCASIONAL,
        SMOKER
    }

    public enum DrinkingHabit {
        NON_DRINKER,
        SOCIAL_DRINKER,
        REGULAR_DRINKER
    }

    public enum MotherTongue {
        HINDI,
        ENGLISH,
        MARATHI,
        TELUGU,
        TAMIL,
        KANNADA,
        MALAYALAM,
        BENGALI,
        GUJARATI,
        PUNJABI,
        ODIA,
        ASSAMESE,
        URDU,
        KONKANI,
        SINDHI,
        NEPALI,
        OTHER // For languages not explicitly listed
    }


    // Custom getter for age, calculated from dateOfBirth
    public Integer getAge() {
        if (this.dateOfBirth != null) {
            return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }
}