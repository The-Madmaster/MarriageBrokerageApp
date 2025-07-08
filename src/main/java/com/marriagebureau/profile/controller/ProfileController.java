package com.marriagebureau.profile.controller;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.profile.service.ProfileService;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final AppUserRepository appUserRepository; // To get the user ID from the authenticated user

    public ProfileController(ProfileService profileService, AppUserRepository appUserRepository) {
        this.profileService = profileService;
        this.appUserRepository = appUserRepository;
    }

    // Helper to get current authenticated user's ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser") {
            throw new IllegalStateException("User is not authenticated.");
        }
        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser currentUser = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database: " + userEmail));
        return currentUser.getId();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Only authenticated users can create their profile
    public ResponseEntity<Profile> createProfileForCurrentUser(@Valid @RequestBody Profile profile) {
        Long currentUserId = getCurrentUserId();
        Profile createdProfile = profileService.createProfile(currentUserId, profile);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Users can view their own profile
    public ResponseEntity<Profile> getMyProfile() {
        Long currentUserId = getCurrentUserId();
        return profileService.getProfileByUserId(currentUserId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @profileService.getProfileById(#id).isPresent() and @profileService.getProfileById(#id).get().getUser().getId() == authentication.principal.id)")
    // Admin can view any profile. A user can view their own profile by its ID.
    // NOTE: For a real dating app, you'd allow users to view other users' profiles, but we're starting with basics.
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Authenticated users can update their own profile
    public ResponseEntity<Profile> updateMyProfile(@Valid @RequestBody Profile profile) {
        Long currentUserId = getCurrentUserId();
        Profile existingProfile = profileService.getProfileByUserId(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for current user. Create one first."));
        
        // Ensure the ID of the profile to update matches the existing profile's ID
        // This prevents users from trying to update other profiles if they somehow get the ID
        return ResponseEntity.ok(profileService.updateProfile(existingProfile.getId(), profile));
    }


    @DeleteMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Authenticated users can delete their own profile
    public ResponseEntity<Void> deleteMyProfile() {
        Long currentUserId = getCurrentUserId();
        Profile existingProfile = profileService.getProfileByUserId(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for current user."));
        profileService.deleteProfile(existingProfile.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can list all profiles
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles()); // Need to add this method in service
    }

    // Add this method to ProfileService
    // public List<Profile> getAllProfiles() {
    //     return profileRepository.findAll();
    // }
}