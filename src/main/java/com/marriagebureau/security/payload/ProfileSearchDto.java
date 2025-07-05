package com.marriagebureau.security.payload; // Corrected package based on error

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchDto {
    private String gender; // Changed from Profile.Gender
    private Integer minAge;
    private Integer maxAge;
    private String religion;
    private String caste;
    private String maritalStatus; // Changed from Profile.MaritalStatus
    private Double minHeightCm;
    private Double maxHeightCm;
    private String city;
    private String state;
    private String country;
    private String education;
    private String occupation;
}