package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Import for validation
import jakarta.validation.constraints.NotNull; // Import for validation
import jakarta.validation.constraints.PastOrPresent; // Import for validation
import jakarta.validation.constraints.Size; // Import for validation
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime; // Added for createdDate/lastUpdatedDate

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false, unique = true)
    @NotNull(message = "AppUser cannot be null") // Added validation
    private AppUser appUser; // This maps to app_user_id in V1 SQL

    @NotBlank(message = "Full name is required") // Added validation
    @Size(max = 255, message = "Full name cannot exceed 255 characters") // Added validation
    private String fullName;

    @NotNull(message = "Date of birth is required") // Added validation
    @PastOrPresent(message = "Date of birth cannot be in the future") // Added validation
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required") // Added validation
    @Size(max = 10, message = "Gender cannot exceed 10 characters") // Added validation
    private String gender; // Male, Female, Other

    @NotBlank(message = "Religion is required") // Added validation
    @Size(max = 50, message = "Religion cannot exceed 50 characters") // Added validation
    private String religion;

    @Size(max = 50, message = "Caste cannot exceed 50 characters") // Added validation
    private String caste;

    @Size(max = 50, message = "Sub-Caste cannot exceed 50 characters") // Added validation
    private String subCaste;

    @Size(max = 20, message = "Marital status cannot exceed 20 characters") // Added validation
    private String maritalStatus; // Single, Married, Divorced, Widowed

    private Double heightCm;
    private String complexion;

    @Size(max = 20, message = "Body type cannot exceed 20 characters") // Added validation
    private String bodyType; // Slim, Athletic, Average, Heavy

    @Size(max = 100, message = "Education cannot exceed 100 characters") // Added validation
    private String education;

    @Size(max = 100, message = "Occupation cannot exceed 100 characters") // Added validation
    private String occupation;

    private Double annualIncome;

    @Size(max = 100, message = "City cannot exceed 100 characters") // Added validation
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters") // Added validation
    private String state;

    @Size(max = 100, message = "Country cannot exceed 100 characters") // Added validation
    private String country;

    @Size(max = 500, message = "About me cannot exceed 500 characters") // Added validation
    private String aboutMe;

    @Size(max = 255, message = "Photo URL cannot exceed 255 characters") // Added validation
    private String photoUrl;

    private boolean isActive = true;

    // Preferred Partner Attributes
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;

    @Size(max = 50, message = "Preferred partner religion cannot exceed 50 characters") // Added validation
    private String preferredPartnerReligion;

    @Size(max = 50, message = "Preferred partner caste cannot exceed 50 characters") // Added validation
    private String preferredPartnerCaste;
    private Double preferredPartnerMinHeightCm;
    private Double preferredPartnerMaxHeightCm;

    // Add timestamps for auditing, matching V1__Create_tables.sql's columns
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) { // Only set if not already set (e.g., by constructor)
            createdDate = LocalDateTime.now();
        }
        lastUpdatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }
}