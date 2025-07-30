// src/main/java/com/marriagebureau/usermanagement/model/AppUser.java
package com.marriagebureau.usermanagement.model; // <--- Package is correct

import com.marriagebureau.brokermanagement.model.BrokerProfile; // <--- Import BrokerProfile (will create this soon)
import com.marriagebureau.usermanagement.entity.AppUserListener;

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
import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
@EntityListeners(AppUserListener.class) // AppUserListener will manage createdDate/lastUpdatedDate

public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String contactNumber;

    @Builder.Default
    private boolean enabled = true; // For account verification (Phase 1.5)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_BROKER; // <--- Default role set to ROLE_BROKER

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate;

    // One-to-one relationship with BrokerProfile (for the broker's own details)
    // 'mappedBy' refers to the field name in the BrokerProfile entity that owns the relationship.
    // We'll define a 'broker' field in BrokerProfile that links back to AppUser.
    @OneToOne(mappedBy = "broker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // <--- Renamed and adjusted mappedBy
    private BrokerProfile brokerProfile; // <--- Renamed 'profile' to 'brokerProfile'


    // --- UserDetails interface methods ---
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // No need for @PrePersist/@PreUpdate here as they are handled by AppUserListener
}