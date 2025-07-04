// src/main/java/com/marriagebureau/usermanagement/entity/AppUser.java
package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections; // For Collections.singletonList

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails { // Implement UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // This will store the encoded password

    // For simplicity, let's assume a single role for now, or you can expand this
    private String role = "ROLE_USER"; // Default role

    // Implementation of UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() { // This method is used for the username in Spring Security
        return this.email; // We use email as the username
    }

    // You can add setters for email and password if Lombok's @Data isn't enough for some operations
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement logic for account expiry
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement logic for account locking
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement logic for credentials expiry
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement logic for account enabling/disabling
    }
}