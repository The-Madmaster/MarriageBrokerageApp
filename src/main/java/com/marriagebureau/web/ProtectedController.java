// src/main/java/com/marriagebureau/web/ProtectedController.java
package com.marriagebureau.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import for @PreAuthorize
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    @GetMapping("/protected-resource")
    @PreAuthorize("isAuthenticated()") // This annotation makes the endpoint truly protected
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("Hello from the PROTECTED resource! You are authenticated.");
    }

    // Example of role-based access
    @GetMapping("/admin-resource")
    @PreAuthorize("hasRole('ADMIN')") // Only users with ROLE_ADMIN can access this
    public ResponseEntity<String> getAdminResource() {
        return ResponseEntity.ok("Hello Admin! This is a highly sensitive resource.");
    }
}