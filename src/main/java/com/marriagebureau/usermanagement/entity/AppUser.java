package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections; // Correctly imported

@Data
@Builder // Lombok's builder pattern
@NoArgsConstructor // Required for JPA
@AllArgsConstructor // Required for @Builder to create a constructor with all fields
@Entity
@Table(name = "app_users") // Ensure table name is correct
@EntityListeners(AppUserListener.class) // Added for automatic date updates (Requires AppUserListener class)
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use email as the primary unique identifier for login/username
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String contactNumber;

    // Use @Builder.Default to ensure these are set when using AppUser.builder().build()
    @Builder.Default
    private boolean enabled = true; // Default to true for new users

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false) // Role should not be null
    @Builder.Default // Default role for builder
    private Role role = Role.ROLE_USER; // Default to a basic user role

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate; // Removed @Builder.Default here, handled by listener

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate; // Removed @Builder.Default here, handled by listener

    // One-to-one relationship with Profile
    // mappedBy indicates the owning side is in Profile (via appUser field)
    // cascade = CascadeType.ALL: Operations on AppUser (e.g., delete) will cascade to Profile
    // orphanRemoval = true: If a Profile is disassociated (set to null), it will be deleted
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile; // Link to the user's profile

    // Enum for roles
    public enum Role {
        ROLE_USER, // Basic user role
        ROLE_ADMIN // Administrator role
    }

    // --- UserDetails interface methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a collection containing the user's role as a SimpleGrantedAuthority
        // Using .name() explicitly for the string representation
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        // Spring Security expects this method to return the principal's username.
        // We are using 'email' as the username for authentication.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement actual logic if needed (e.g., based on expiration date)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement actual logic if needed (e.g., based on failed login attempts)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement actual logic if needed (e.g., forcing password reset)
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // --- Lifecycle Callbacks (Moved to AppUserListener for cleaner entity) ---
    // If you prefer, these can be kept here with @PrePersist and @PreUpdate
}