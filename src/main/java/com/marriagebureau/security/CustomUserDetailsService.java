// src/main/java/com/marriagebureau/security/CustomUserDetailsService.java

package com.marriagebureau.security;

import com.marriagebureau.entity.AppUser; // Corrected: AppUser is in 'com.marriagebureau.entity'
// No direct import needed for UserRole here, as it's part of AppUser (unless you explicitly use it somewhere else in this file)
// If you do, it should also be: import com.marriagebureau.entity.UserRole;

import com.marriagebureau.usermanagement.service.AppUserService; // This path is correct for the service

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;

    public CustomUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Using your existing service method to find the AppUser
        AppUser appUser = appUserService.findByUsername(username)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert your AppUser roles to Spring Security GrantedAuthorities
        // Assuming AppUser.getRoles() returns a Collection<UserRole>
        Collection<? extends GrantedAuthority> authorities = appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Roles must be prefixed with "ROLE_"
                .collect(Collectors.toList());

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}