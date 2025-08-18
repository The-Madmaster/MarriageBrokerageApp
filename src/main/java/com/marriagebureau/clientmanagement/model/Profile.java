package com.marriagebureau.clientmanagement.model;

import com.marriagebureau.clientmanagement.model.enums.*;
import com.marriagebureau.usermanagement.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_profiles")
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    private AppUser broker;

    // --- Core Personal Details ---
    @Column(nullable = false)
    private String fullName;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    private Integer heightCm;

    // --- Cultural/Background Details ---
    private String religion;
    private String caste;
    private String subCaste;
    @Enumerated(EnumType.STRING)
    private MotherTongue motherTongue;

    // --- Location ---
    private String country;
    private String state;
    private String city;
    
    // --- Physical Attributes ---
    @Enumerated(EnumType.STRING)
    private Complexion complexion;
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    // --- Professional/Educational Details ---
    private String education;
    private String occupation;
    private Double annualIncome;

    // --- Lifestyle & Habits ---
    @Enumerated(EnumType.STRING)
    private Diet diet;
    @Enumerated(EnumType.STRING)
    private SmokingHabit smokingHabit;
    @Enumerated(EnumType.STRING)
    private DrinkingHabit drinkingHabit;

    // --- About Me & Photos ---
    @Column(length = 1000)
    private String aboutMe;
    private String photoUrl;

    @Builder.Default
    private boolean isActive = true;

    // --- Preferred Partner Criteria ---
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    private String preferredPartnerReligion;
    private String preferredPartnerCaste;
    private Integer preferredPartnerMinHeightCm;
    private Integer preferredPartnerMaxHeightCm;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastUpdatedDate;

    public Integer getAge() {
        if (this.dateOfBirth != null) {
            return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }
}