package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.service.ProfileService;
import com.marriagebureau.usermanagement.service.BiodataPdfService;
import com.lowagie.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    private final ProfileService profileService;
    private final BiodataPdfService biodataPdfService; // Ensure this is imported and injected

    public ProfileController(ProfileService profileService, BiodataPdfService biodataPdfService) {
        this.profileService = profileService;
        this.biodataPdfService = biodataPdfService;
    }

    // --- Create Profile ---
    @PostMapping("/users/{userId}/profiles")
    public ResponseEntity<Profile> createProfile(
            @PathVariable Long userId, // Corresponds to appUser ID
            @RequestBody Profile profile) {
        Profile createdProfile = profileService.createProfile(userId, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    // ... (other methods like getAllProfiles, getProfileById remain similar)

    // --- Update Profile ---
    @PutMapping("/users/{userId}/profiles/{profileId}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long userId,
            @PathVariable Long profileId,
            @RequestBody Profile profileDetails) {
        Profile updatedProfile = profileService.updateProfile(userId, profileId, profileDetails);
        return ResponseEntity.ok(updatedProfile);
    }

    // --- Delete Profile ---
    @DeleteMapping("/users/{userId}/profiles/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long userId,
            @PathVariable Long profileId) {
        profileService.deleteProfile(userId, profileId);
        return ResponseEntity.noContent().build();
    }

    // --- Search Profiles ---
    // Update @RequestParam names and types to match your Profile entity and service method
    @GetMapping("/profiles/search")
    public ResponseEntity<List<Profile>> searchProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String caste,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double minHeightCm, // Changed from minHeight
            @RequestParam(required = false) Double maxHeightCm, // Changed from maxHeight
            @RequestParam(required = false) String maritalStatus, // Added
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country
    ) {
        List<Profile> profiles = profileService.searchProfiles(
                gender, religion, caste, minAge, maxAge,
                minHeightCm, maxHeightCm, maritalStatus,
                education, occupation, city, state, country
        );
        return ResponseEntity.ok(profiles);
    }

    // --- Download Biodata PDF ---
    // This is a common method signature, ensure BiodataPdfService has it.
    @GetMapping("/profiles/{profileId}/biodata-pdf")
    public ResponseEntity<byte[]> downloadBiodataPdf(@PathVariable Long profileId) {
        Optional<Profile> profileOptional = profileService.getProfileById(profileId);
        if (profileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Profile profile = profileOptional.get();

        try {
            // Ensure BiodataPdfService has public byte[] generateBiodataPdf(Profile profile)
            byte[] pdfBytes = biodataPdfService.generateBiodataPdf(profile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "biodata-" + profileId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}