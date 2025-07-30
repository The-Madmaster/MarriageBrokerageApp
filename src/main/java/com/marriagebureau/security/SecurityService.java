// src/main/java/com/marriagebureau/security/SecurityService.java
package com.marriagebureau.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marriagebureau.usermanagement.model.AppUser;

@Service
public class SecurityService {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getId(); // If you are still using UserPrincipal
        } else if (principal instanceof AppUser) {
            return ((AppUser) principal).getId(); // If AppUser directly implements UserDetails
        } else {
            // Handle other types if necessary, or throw an exception
            return null;
        }
    }
}