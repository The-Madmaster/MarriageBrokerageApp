package com.marriagebureau.clientmanagement.model;

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

@Entity
@Table(name = "client_profiles") // Renamed table for clarity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // For automatic timestamps
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELATIONSHIP CHANGE ---
    // Switched from @OneToOne to @ManyToOne.
    // Many client profiles can now belong to one broker.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", referencedColumnName = "id", nullable = false)
    private AppUser broker;

    // --- Core Personal Details ---
    @Column(name = "full_name", nullable = false)
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

    // ... other fields remain the same ...
    // education, occupation, income, diet, habits, aboutMe, photoUrl etc.

    // --- Preferred Partner Criteria ---
    private Integer preferredPartnerMinAge;
    private Integer preferredPartnerMaxAge;
    // ... other partner preference fields ...

    // --- Account Status & Timestamps ---
    @Builder.Default
    private boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastUpdatedDate;

    // --- Custom Getter for Age ---
    public Integer getAge() {
        if (this.dateOfBirth != null) {
            return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }
}