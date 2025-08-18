package com.marriagebureau.clientmanagement.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for partially updating an existing client profile.
 * All fields are optional. Validation is applied only if a value is provided.
 */
@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String fullName;

    @Size(max = 200)
    private String occupation; // <-- Field needed for the test

    @PositiveOrZero
    private Double annualIncome;

    private Boolean isActive;
}