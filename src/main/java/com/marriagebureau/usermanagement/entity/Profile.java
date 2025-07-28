package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", unique = true, nullable = false)
    private AppUser appUser;

    // --- Core Personal Details ---
    @Column(name = "full_name", nullable = false)
    private String fullName;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer heightCm;

    // --- Cultural/Background Details ---
    private String religion;
    private String caste;
    private String subCaste;

    @Enumerated(EnumType.STRING)
    private MotherTongue motherTongue;

    private String country;
    private String state;
    private String city;

    // --- Physical Attributes ---
    @Enumerated(EnumType.STRING)
    private Complexion complexion;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    // --- Professional/Educational Details ---
    private String education;
    private String occupation;
    private Double annualIncome;

    // --- Lifestyle & Habits ---
    @Enumerated(EnumType.STRING)
    private Diet diet;

    @Enumerated(EnumType.STRING)
    private SmokingHabit smokingHabit;

    @Enumerated(EnumType.STRING)
    private DrinkingHabit drinkingHabit;

    // --- About Me & Photos ---
    @Column(length = 1000)
    private String aboutMe;
    private String photoUrl;

    // --- Account Status ---
    @Builder.Default
    private boolean isActive = true;

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
        ANNULLED
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
        EGGETARIAN
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
        OTHER
    }

    // Custom getter for age, calculated from dateOfBirth
    public Integer getAge() {
        if (this.dateOfBirth != null) {
            return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }
}