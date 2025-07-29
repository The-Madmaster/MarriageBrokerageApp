package com.marriagebureau.brokermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // For createdDate and lastUpdatedDate

import com.marriagebureau.usermanagement.entity.AppUser;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "broker_profiles") // Name your database table for broker profiles
public class BrokerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one relationship with AppUser.
    // BrokerProfile is the owning side of the relationship,
    // meaning this table will contain the foreign key to the app_users table.
    // 'fetch = FetchType.LAZY' is recommended for performance.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", unique = true, nullable = false) // Foreign key column for AppUser
    private AppUser broker; // The field name 'broker' matches 'mappedBy = "broker"' in AppUser

    @Column(nullable = false)
    private String brokerName; // The name of the individual broker

    @Column(nullable = true) // Firm name is optional
    private String firmName;

    @Column(unique = true, nullable = false) // Registration number should typically be unique
    private String registrationNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = true) // Allowing it to be optional, as AppUser also has a contact number
    private String firmContactNumber; // Contact number specific to the firm or an alternate broker contact

    // Audit fields for tracking creation and updates
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate;

    // Lifecycle callbacks to automatically set audit dates
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.lastUpdatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedDate = LocalDateTime.now();
    }
}