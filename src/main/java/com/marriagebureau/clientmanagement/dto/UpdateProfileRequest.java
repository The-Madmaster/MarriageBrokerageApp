package com.marriagebureau.clientmanagement.dto;

import com.marriagebureau.clientmanagement.model.enums.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

/**
 * DTO for partially updating an existing client profile.
 * All fields are optional. Validation is applied only if a value is provided.
 */
@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String fullName;

    @Past
    private LocalDate dateOfBirth;

    private Gender gender;
    private MaritalStatus maritalStatus;

    @Min(50) @Max(300)
    private Integer heightCm;
    
    // --- Cultural/Background Details ---
    @Size(max = 50)
    private String religion;
    
    @Size(max = 50)
    private String caste;
    
    // ... include any other fields from Profile that you want to be updatable
    
    private Boolean isActive;

    @URL(message = "Photo URL must be a valid URL")
    private String photoUrl;
}