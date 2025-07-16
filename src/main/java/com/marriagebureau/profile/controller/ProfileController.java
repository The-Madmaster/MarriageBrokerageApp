// src/main/java/com/marriagebureau/profile/controller/ProfileController.java

package com.marriagebureau.profile.controller;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.profile.service.ProfileService;
import jakarta.validation.Valid; // For @Valid annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Import Authentication
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Creates a new user profile.
     * Accessible to ROLE_USER (for creating their own profile after registration)
     * and ROLE_ADMIN.
     *
     * The appUserId path variable should match the authenticated user's ID
     * or be managed by an admin.
     * This example assumes the appUserId in the URL is for the user being associated.
     * In a real app, a user would create their OWN profile,
     * so appUserId might come from Authentication or be omitted.
     */
    @PostMapping("/{appUserId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #appUserId == authentication.principal.id)")
    public ResponseEntity<Profile> createProfile(@PathVariable Long appUserId, @Valid @RequestBody Profile profile) {
        try {
            Profile createdProfile = profileService.createProfile(profile, appUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            // General catch-all for other unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating profile: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves a user profile by profile ID.
     * Accessible to ROLE_ADMIN (any profile)
     * or ROLE_USER (only their own profile based on appUser.id).
     */
    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @profileService.getProfileById(#profileId).orElse(new com.marriagebureau.usermanagement.entity.Profile()).getAppUser().getId() == authentication.principal.id)")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long profileId, Authentication authentication) {
        // Note: The @PreAuthorize handles the authorization logic.
        // We just need to fetch the profile here.

        return profileService.getProfileById(profileId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with ID: " + profileId));
    }


    /**
     * Retrieves a user profile by AppUser ID.
     * This is useful if you authenticate a user and then want to find THEIR profile.
     * Accessible to ROLE_ADMIN (any profile by appUser ID)
     * or ROLE_USER (only their own profile using their authenticated ID).
     */
    @GetMapping("/user/{appUserId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #appUserId == authentication.principal.id)")
    public ResponseEntity<Profile> getProfileByAppUserId(@PathVariable Long appUserId) {
        return profileService.getProfileByAppUserId(appUserId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for AppUser ID: " + appUserId));
    }


    /**
     * Updates an existing user profile by profile ID.
     * Accessible to ROLE_ADMIN (any profile)
     * or ROLE_USER (only their own profile).
     */
    @PutMapping("/{profileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @profileService.getProfileById(#profileId).orElse(new com.marriagebureau.usermanagement.entity.Profile()).getAppUser().getId() == authentication.principal.id)")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long profileId, @Valid @RequestBody Profile profile) {
        Profile updatedProfile = profileService.updateProfile(profileId, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Deletes a user profile by profile ID.
     * Accessible to ROLE_ADMIN (any profile)
     * or ROLE_USER (only their own profile).
     */
    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @profileService.getProfileById(#profileId).orElse(new com.marriagebureau.usermanagement.entity.Profile()).getAppUser().getId() == authentication.principal.id)")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }
}