package com.marriagebureau.clientmanagement.dto;

import com.marriagebureau.clientmanagement.model.enums.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

/**
 * DTO for creating a new client profile.
 * Contains strong validation for all mandatory fields.
 */
@Data
public class CreateProfileRequest {

    // --- Core Personal Details ---
    @NotBlank(message = "Full name is mandatory")
    @Size(max = 100)
    private String fullName;

    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of Birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private Gender gender;

    @NotNull(message = "Marital Status is mandatory")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Height is mandatory")
    @Min(50) @Max(300)
    private Integer heightCm;

    // --- Cultural/Background Details ---
    @NotBlank(message = "Religion is mandatory")
    @Size(max = 50)
    private String religion;

    @NotBlank(message = "Caste is mandatory")
    @Size(max = 50)
    private String caste;

    @Size(max = 50)
    private String subCaste; // Optional

    @NotNull(message = "Mother Tongue is mandatory")
    private MotherTongue motherTongue;

    // --- Location ---
    @NotBlank(message = "Country is mandatory")
    private String country;
    
    @NotBlank(message = "State is mandatory")
    private String state;
    
    @NotBlank(message = "City is mandatory")
    private String city;

    // --- Professional/Educational Details ---
    @NotBlank(message = "Education is mandatory")
    @Size(max = 200)
    private String education;

    @NotBlank(message = "Occupation is mandatory")
    @Size(max = 200)
    private String occupation;

    @NotNull(message = "Annual Income is mandatory")
    @PositiveOrZero
    private Double annualIncome;
    
    // --- Other Details ---
    @URL(message = "Photo URL must be a valid URL")
    private String photoUrl; // Optional
}