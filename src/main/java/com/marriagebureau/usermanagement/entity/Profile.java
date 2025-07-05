package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private AppUser appUser;

    private String fullName;
    private LocalDate dateOfBirth;
    private String gender; // Male, Female, Other
    private String religion;
    private String caste;
    private String subCaste;
    private String maritalStatus; // Single, Married, Divorced, Widowed
    private Double heightCm;
    private String complexion;
    private String bodyType; // Slim, Athletic, Average, Heavy
    private String education;
    private String occupation;
    private Double annualIncome;
    private String city;
    private String state;
    private String country;
    private String aboutMe;
    private String photoUrl;
    private boolean isActive = true;

    // Preferred Partner Attributes
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion;
    private String preferredPartnerCaste;
    private Double preferredPartnerMinHeightCm;
    private Double preferredPartnerMaxHeightCm;
}