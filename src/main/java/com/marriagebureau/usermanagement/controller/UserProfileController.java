package com.marriagebureau.usermanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections; // Import for Collections.singletonMap

@RestController
@RequestMapping("/api")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @GetMapping("/user-profiles/me")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getMyProfile(Principal principal) { // Change return type to ResponseEntity<?>
        logger.debug("--- UserProfileController: Entering /user-profiles/me endpoint ---");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Attempted to access /user-profiles/me endpoint without authenticated user in controller. This indicates a serious security configuration issue.");
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Unauthorized access within controller.")); // Return JSON error
        }

        // Return a JSON object with user details
        return ResponseEntity.ok(Collections.singletonMap("message", "Hello, " + principal.getName() + "! You can access your profile because you are authenticated as a USER or ADMIN."));
    }

    @GetMapping("/v1/users/me")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getMyProfileLegacy(Principal principal) { // Change return type to ResponseEntity<?>
        logger.debug("--- UserProfileController: Entering /v1/users/me (legacy) endpoint ---");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Attempted to access /v1/users/me (legacy) endpoint without authenticated user in controller. This indicates a serious security configuration issue.");
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Unauthorized access within controller.")); // Return JSON error
        }

        // Return a JSON object with user details
        return ResponseEntity.ok(Collections.singletonMap("message", "Hello from legacy endpoint, " + principal.getName() + "! You can access your profile because you are authenticated as a USER or ADMIN."));
    }

    // ... other methods (admin-only, public-data) would also ideally return JSON
    @GetMapping("/user-profiles/admin-only")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAdminDashboard(Authentication authentication) {
        return ResponseEntity.ok(Collections.singletonMap("message", "Welcome, Administrator " + authentication.getName() + "! This content is only for admins."));
    }

    @GetMapping("/user-profiles/public-data")
    public ResponseEntity<?> getPublicData() {
        return ResponseEntity.ok(Collections.singletonMap("message", "This data is available to any authenticated user."));
    }
}