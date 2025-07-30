package com.marriagebureau.clientmanagement.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for capturing search criteria for client profiles.
 * Includes filters, pagination, and sorting parameters. All fields are optional.
 */
@Data
@Builder
public class ProfileSearchRequest {
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private String country;
    private String state;
    private String city;
    private String religion;
    private String caste;
    private String maritalStatus;
    private Integer minHeightCm;
    private Integer maxHeightCm;

    // Pagination and Sorting
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "asc";
}