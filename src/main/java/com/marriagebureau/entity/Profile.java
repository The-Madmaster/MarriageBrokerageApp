package com.marriagebureau.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity
@Table(name = "profiles") // Specifies the table name in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
public class Profile {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Stores enum names as strings
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 50)
    private String religion;

    @Column(length = 50)
    private String caste;

    @Column(length = 50)
    private String subCaste;

    @Column(length = 100)
    private String education;

    @Column(length = 100)
    private String occupation;

    @Column
    private Double annualIncome; // Using Double for potentially variable income

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column
    private Double heightCm; // Height in centimeters

    @Column(length = 50)
    private String complexion; // <--- ADDED 'private' KEYWORD

    @Column(length = 50)
    private String bodyType;

    @Lob // Large Object, suitable for long text
    private String aboutMe;

    @Column(length = 20)
    private String contactNumber; // Contact specific to the profile

    @Column(unique = true, length = 100) // Email unique per profile
    private String email;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    @Column(length = 255)
    private String photoUrl; // URL to profile picture

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedDate;

    @Column(nullable = false)
    private boolean isActive = true; // Flag to activate/deactivate profile

    // --- Preferred Partner Details (simple example) ---
    @Column(length = 50)
    private String preferredPartnerReligion;

    @Column(length = 50)
    private String preferredPartnerCaste;

    @Column
    private Integer preferredPartnerMinAge;

    @Column
    private Integer preferredPartnerMaxAge;

    @Column
    private Double preferredPartnerMinHeightCm;

    @Column
    private Double preferredPartnerMaxHeightCm;

    // Relationship: Many Profiles can be managed by one User (Broker/Admin)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching for performance
    @JoinColumn(name = "created_by_user_id", nullable = false) // Foreign key column
    private User createdByUser; // <--- RENAMED FROM 'createdBy' TO 'createdByUser'

    // Lifecycle callbacks for auditing createdDate and lastUpdatedDate
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastUpdatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }
}