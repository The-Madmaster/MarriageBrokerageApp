// src/main/java/com/marriagebureau/security/UserPrincipal.java
package com.marriagebureau.security;

import com.marriagebureau.usermanagement.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List; // Import List

// This class might become redundant if AppUser directly implements UserDetails
// But if you still need it for some reason, here's the fix.
public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(AppUser appUser) {
        // AppUser's getAuthorities method should return the list of authorities
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) appUser.getAuthorities();

        return new UserPrincipal(
                appUser.getId(),
                appUser.getEmail(), // Use getEmail()
                appUser.getPassword(), // Use getPassword()
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Use email as username
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
        return true;
    }
}