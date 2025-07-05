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
import java.util.Collections; // Make sure to import this

@Data
@Builder // Lombok's builder pattern
@NoArgsConstructor // Required for JPA
@AllArgsConstructor // Required for @Builder to create a constructor with all fields
@Entity
@Table(name = "app_users") // Ensure table name is correct
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use email as the primary unique identifier for login/username
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Removed the separate 'username' field to avoid redundancy,
    // as 'email' will serve as the username for Spring Security.
    // If you need a display name, you can add a 'displayName' field.

    private String contactNumber;

    // Use @Builder.Default to ensure these are set when using AppUser.builder().build()
    @Builder.Default
    private boolean enabled = true; // Default to true for new users

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false) // Role should not be null
    @Builder.Default // Default role for builder
    private Role role = Role.ROLE_USER; // Default to a basic user role

    @Builder.Default
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now(); // Initialize for direct new AppUser() or @Builder.Default

    @Builder.Default
    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate = LocalDateTime.now(); // Initialize for direct new AppUser() or @Builder.Default


    // Enum for roles (create this if you haven't)
    public enum Role {
        ROLE_USER, // Basic user role
        ROLE_ADMIN // Administrator role
    }

    // --- UserDetails interface methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a collection containing the user's role as a SimpleGrantedAuthority
        // The toString() of the Role enum will give "ROLE_USER" or "ROLE_ADMIN"
        if (this.role != null) {
            return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
        }
        return Collections.emptyList(); // Return empty list if role is null (shouldn't happen with proper data)
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
}