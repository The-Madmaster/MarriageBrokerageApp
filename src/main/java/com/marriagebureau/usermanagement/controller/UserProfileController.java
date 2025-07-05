package com.marriagebureau.usermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal; // Import java.security.Principal

@RestController
@RequestMapping("/api/user-profiles") // Base path for user profile related endpoints
public class UserProfileController {

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") // Only users with ROLE_USER or ROLE_ADMIN
    public ResponseEntity<String> getMyProfile(Principal principal) {
        // principal.getName() will return the username (email in your case) from the authenticated user
        String responseMessage = "Hello, " + principal.getName() + "! You can access your profile because you are authenticated as a USER or ADMIN.";
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only users with ROLE_ADMIN
    public ResponseEntity<String> getAdminDashboard(Authentication authentication) {
        String responseMessage = "Welcome, Administrator " + authentication.getName() + "! This content is only for admins.";
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/public-data")
    public ResponseEntity<String> getPublicData() {
        // This endpoint is not secured by @PreAuthorize, but will fall under .anyRequest().authenticated()
        // in SecurityConfig, unless you explicitly add a .permitAll() for it.
        // For now, it will require authentication by default.
        return ResponseEntity.ok("This data is available to any authenticated user.");
    }
}