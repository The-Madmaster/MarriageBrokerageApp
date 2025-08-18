package com.marriagebureau.security;

import com.marriagebureau.usermanagement.model.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    /**
     * Retrieves the user ID of the currently authenticated user.
     * @return The Long ID of the AppUser, or null if not authenticated.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // In our design, the principal is always an AppUser instance.
        if (principal instanceof AppUser) {
            return ((AppUser) principal).getId();
        }
        
        return null;
    }
}