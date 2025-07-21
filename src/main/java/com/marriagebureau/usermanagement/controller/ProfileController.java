// src/main/java/com/marriagebureau/usermanagement/controller/ProfileController.java
package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.profiles.dto.ProfileDTO; // Import the ProfileDTO
import com.marriagebureau.profiles.dto.ProfileSearchRequest; // Import the ProfileSearchRequest
import com.marriagebureau.usermanagement.entity.Profile; // Assuming Profile is your entity for profile data
import com.marriagebureau.usermanagement.service.ProfileService;
import com.marriagebureau.usermanagement.service.BiodataPdfService; // ⭐ NEW: Import BiodataPdfService
import org.springframework.data.domain.Page; // Import Page for paginated results
import org.springframework.data.domain.Pageable; // Import Pageable for pagination
import org.springframework.data.web.PageableDefault; // Import for default Pageable parameters
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Add PreAuthorize for security
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal; // ⭐ NEW: Import Principal for authorization checks
import java.util.List; // Keep if other methods return List, but search will return Page
import java.util.Objects; // ⭐ NEW: For Objects.equals()
import java.util.Optional;

// OpenPDF specific imports that will be needed by BiodataPdfService, but also mentioned here for context if any exceptions from it are caught.
// import com.github.librepdf.openpdf.text.DocumentException; // This is an OpenPDF exception

@RestController
@RequestMapping("/api/v1") // Keep your /api/v1 base path
public class ProfileController {

    private final ProfileService profileService;
    private final BiodataPdfService biodataPdfService; // ⭐ NEW: Inject BiodataPdfService

    public ProfileController(ProfileService profileService, BiodataPdfService biodataPdfService) { // ⭐ Constructor adjusted to inject BiodataPdfService
        this.profileService = profileService;
        this.biodataPdfService = biodataPdfService; // ⭐ Initialize new service
    }

    // --- Create Profile ---
    @PostMapping("/users/{userId}/profiles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Example: User can create their own profile, admin for any
    public ResponseEntity<Profile> createProfile(
            @PathVariable Long userId, // Corresponds to appUser ID
            @RequestBody Profile profile) {
        Profile createdProfile = profileService.createProfile(userId, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    // --- Get All Profiles (if needed, consider pagination) ---
    // This method now correctly accepts a Pageable parameter and returns Page<ProfileDTO>
    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProfileDTO>> getAllProfiles(
            @PageableDefault(size = 10, page = 0, sort = "id") Pageable pageable) { // Add Pageable
        Page<ProfileDTO> profiles = profileService.getAllProfiles(pageable); // Pass Pageable to service
        return ResponseEntity.ok(profiles);
    }

    // --- Get Profile by ID ---
    @GetMapping("/profiles/{profileId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long profileId) {
        Optional<Profile> profile = profileService.getProfileById(profileId);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- Update Profile ---
    @PutMapping("/users/{userId}/profiles/{profileId}")
    @PreAuthorize("hasRole('USER') and @securityService.isOwner(#userId) or hasRole('ADMIN')") // Example: User is owner OR Admin
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long userId,
            @PathVariable Long profileId,
            @RequestBody Profile profileDetails) {
        Profile updatedProfile = profileService.updateProfile(userId, profileId, profileDetails);
        return ResponseEntity.ok(updatedProfile);
    }

    // --- Delete Profile ---
    @DeleteMapping("/users/{userId}/profiles/{profileId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityService.isOwner(#userId))") // Example: Only Admin or User can delete their own
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long userId,
            @PathVariable Long profileId) {
        profileService.deleteProfile(userId, profileId);
        return ResponseEntity.noContent().build();
    }

    // --- Search Profiles (UPDATED TO USE DTO AND PAGE) ---
    @GetMapping("/profiles/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Only authenticated users can search
    public ResponseEntity<Page<ProfileDTO>> searchProfiles(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String caste,
            @RequestParam(required = false) String subCaste,
            @RequestParam(required = false) String educationLevel,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer minHeightCm,
            @RequestParam(required = false) Integer maxHeightCm,
            @RequestParam(required = false) String motherTongue,
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String smokingHabit,
            @RequestParam(required = false) String drinkingHabit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        ProfileSearchRequest searchRequest = new ProfileSearchRequest(
                minAge, maxAge, country, state, city, religion, caste, subCaste,
                educationLevel, profession, maritalStatus, gender,
                minHeightCm, maxHeightCm, motherTongue, diet, smokingHabit, drinkingHabit,
                page, size, sortBy, sortDirection
        );

        // Call the service method that returns Page<ProfileDTO>
        Page<ProfileDTO> profiles = profileService.searchProfiles(searchRequest);
        return ResponseEntity.ok(profiles);
    }

    // --- Download Biodata PDF --- ⭐ NEW ENDPOINT ADDED HERE ⭐
    @GetMapping("/profiles/{profileId}/biodata-pdf")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Only authenticated users
    public ResponseEntity<byte[]> downloadBiodataPdf(@PathVariable Long profileId, Principal principal) {
        // 1. Authorization check: Ensure 'principal' (authenticated user) is authorized to view 'profileId's biodata.
        //    This means:
        //    - If the user is an ADMIN, they can access any profile's biodata.
        //    - If the user is a USER, they can only access their *own* profile's biodata.
        //    You'll need a mechanism to link the authenticated principal's ID to a profile.
        //    Assuming `principal.getName()` gives you the `AppUser` ID, and you need to find the `profileId` associated with it.

        Optional<Profile> profileOptional = profileService.getProfileById(profileId); // Fetch the profile
        if (profileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Profile profile = profileOptional.get();

        // Basic authorization check (enhance with proper Spring Security expression/service if needed)
        // This assumes `principal.getName()` returns the `AppUser` ID as a String.
        // And the `Profile` entity has a link back to the `AppUser` ID (e.g., `profile.getAppUser().getId()`).
        boolean isAdmin = principal != null && principal.getName().contains("admin_role_identifier"); // Replace with actual role check
        boolean isOwner = principal != null && Objects.equals(profile.getAppUser().getId(), Long.valueOf(principal.getName()));

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // HTTP 403 Forbidden
        }

        try {
            // 2. Data Retrieval & 3. PDF Generation:
            // The service method will retrieve all necessary data for the PDF from the 'profile' object
            byte[] pdfBytes = biodataPdfService.generateBiodataPdf(profile); // Pass the fetched Profile entity

            // 4. Set HTTP Headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "biodata-" + profileId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // 5. Return as ResponseEntity
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) { // Catch generic Exception, or specific ones like DocumentException if you add them
            // Log the exception properly with a logger (e.g., SLF4J)
            e.printStackTrace(); // For debugging, replace with proper logging framework
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // HTTP 500 Internal Server Error
        }
    }
}