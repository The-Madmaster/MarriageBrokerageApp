package com.marriagebureau.usermanagement.controller; // Corrected package name

import com.marriagebureau.entity.AppProfile; // Changed import from Profile to AppProfile
import com.marriagebureau.usermanagement.service.ProfileService; // Corrected service import
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles") // Base URL for all profile-related endpoints
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Endpoint to create a new profile
    @PostMapping
    public ResponseEntity<AppProfile> createProfile(@RequestBody AppProfile profile, @RequestParam Long userId) { // Changed Profile to AppProfile
        AppProfile createdProfile = profileService.createProfile(profile, userId); // Changed Profile to AppProfile
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    // Endpoint to get a profile by its ID
    @GetMapping("/{id}")
    public ResponseEntity<AppProfile> getProfileById(@PathVariable Long id) { // Changed Profile to AppProfile
        return profileService.getProfileById(id)
                .map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to get a profile by the associated User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<AppProfile> getProfileByUserId(@PathVariable Long userId) { // Changed Profile to AppProfile
        return profileService.getProfileByUserId(userId)
                .map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to get a profile by email
    @GetMapping("/byEmail")
    public ResponseEntity<AppProfile> getProfileByEmail(@RequestParam String email) { // Changed Profile to AppProfile
        return profileService.getProfileByEmail(email)
                .map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to get all profiles
    @GetMapping
    public ResponseEntity<List<AppProfile>> getAllProfiles() { // Changed List<Profile> to List<AppProfile>
        List<AppProfile> profiles = profileService.getAllProfiles(); // Changed List<Profile> to List<AppProfile>
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    // Endpoint to update an existing profile
    @PutMapping("/{id}")
    public ResponseEntity<AppProfile> updateProfile(@PathVariable Long id, @RequestBody AppProfile profileDetails) { // Changed Profile to AppProfile
        try {
            AppProfile updatedProfile = profileService.updateProfile(id, profileDetails); // Changed Profile to AppProfile
            return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        try {
            profileService.deleteProfile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if profile doesn't exist
        }
    }
}