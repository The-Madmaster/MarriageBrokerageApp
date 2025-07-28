// src/main/java/com/marriagebureau/profiles/dto/ProfileSearchRequest.java
package com.marriagebureau.profiles.dto;

import lombok.Data;
import lombok.Builder; // Added @Builder for easier test object creation if needed

// Note: No @NotBlank/@NotNull here, as all search parameters are optional
@Data
@Builder // Useful for building search queries in tests or from frontend defaults
public class ProfileSearchRequest {
    private Integer minAge;
    private Integer maxAge;
    private String gender; // Will map to Profile.Gender enum
    private String country;
    private String state;
    private String city;
    private String religion;
    private String caste;
    private String subCaste;
    private String educationLevel; // Maps to 'education' field in Profile
    private String profession;     // Maps to 'occupation' field in Profile
    private String maritalStatus;  // Will map to Profile.MaritalStatus enum
    private Integer minHeightCm;
    private Integer maxHeightCm;
    private String motherTongue;   // Will map to Profile.MotherTongue enum
    private String diet;           // Will map to Profile.Diet enum
    private String smokingHabit;   // Will map to Profile.SmokingHabit enum
    private String drinkingHabit;  // Will map to Profile.DrinkingHabit enum

    // Pagination and Sorting (standard fields for paginated searches)
    @Builder.Default // Default page to 0 if not provided
    private int page = 0;

    @Builder.Default // Default size to 10 if not provided
    private int size = 10;

    @Builder.Default // Default sort by ID if not provided
    private String sortBy = "id";

    @Builder.Default // Default sort direction to ASC if not provided
    private String sortDirection = "asc";
}