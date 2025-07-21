// src/main/java/com/marriagebureau/profiles/dto/ProfileSearchRequest.java
package com.marriagebureau.profiles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class ProfileSearchRequest {

    // Age Range
    private Integer minAge;
    private Integer maxAge;

    // Location
    private String country;
    private String state;
    private String city;

    // Religion & Caste
    private String religion;
    private String caste;
    private String subCaste;

    // Education & Profession
    private String educationLevel;
    private String profession;

    // Marital Status
    private String maritalStatus;

    // Gender of the *desired partner* (assuming the current user implicitly provides their gender)
    private String gender;

    // Height Range
    private Integer minHeightCm; // in centimeters
    private Integer maxHeightCm; // in centimeters

    // Mother Tongue
    private String motherTongue;

    // Dietary Preferences
    private String diet;

    // Habits
    private String smokingHabit;
    private String drinkingHabit;

    // Pagination and Sorting (Optional but good practice for search results)
    private Integer page;
    private Integer size;
    private String sortBy; // e.g., "age", "lastUpdatedDate"
    private String sortDirection; // "asc" or "desc"
}