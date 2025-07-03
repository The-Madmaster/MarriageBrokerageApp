package com.marriagebureau.usermanagement.controller;

import com.lowagie.text.DocumentException; // IMPORTANT: New Import for PDF
import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.service.BiodataPdfService; // IMPORTANT: New Import for PDF Service
import com.marriagebureau.usermanagement.service.ProfileService;
import com.marriagebureau.security.UserPrincipal;
import com.marriagebureau.security.payload.ProfileSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders; // IMPORTANT: New Import for PDF response headers
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // IMPORTANT: New Import for PDF media type
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream; // IMPORTANT: New Import for PDF stream
import java.io.IOException; // IMPORTANT: New Import for PDF stream errors
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final BiodataPdfService biodataPdfService; // IMPORTANT: New field for PDF Service

    @Autowired
    public ProfileController(ProfileService profileService, BiodataPdfService biodataPdfService) { // IMPORTANT: Constructor updated
        this.profileService = profileService;
        this.biodataPdfService = biodataPdfService; // IMPORTANT: Initialize new field
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile, Authentication authentication) {
        Long userId = null;
        if (authentication.getPrincipal() instanceof UserPrincipal) {
            userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Set AppUser to null before processing to avoid circular serialization issues in response
        profile.setAppUser(null);

        Optional<Profile> newProfile = profileService.createProfile(profile, userId);

        return newProfile.map(savedProfile -> {
            savedProfile.setAppUser(null); // Nullify again for response
            return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/my-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> getMyProfile(Authentication authentication) {
        Long userId = null;
        if (authentication.getPrincipal() instanceof UserPrincipal) {
            userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return profileService.getProfileByAppUserId(userId)
                .map(profile -> {
                    profile.setAppUser(null);
                    return new ResponseEntity<>(profile, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id)
                .map(profile -> {
                    profile.setAppUser(null);
                    return new ResponseEntity<>(profile, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        // CORRECTION: Changed from findAllProfiles() to getAllProfiles() to match ProfileService
        List<Profile> profiles = profileService.getAllProfiles();
        List<Profile> profilesWithoutAppUser = profiles.stream()
                .peek(profile -> profile.setAppUser(null))
                .collect(Collectors.toList());
        return new ResponseEntity<>(profilesWithoutAppUser, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') and @securityService.isOwner(#id)")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        Optional<Profile> profile = profileService.updateProfile(id, updatedProfile);
        return profile.map(p -> {
            p.setAppUser(null);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        if (profileService.deleteProfile(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Searches for profiles based on various criteria.
     * GET /api/profiles/search
     *
     * @param searchDto The DTO containing search parameters. Spring will bind query parameters to this object.
     * @return ResponseEntity with a list of matching profiles.
     */
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()") // Only authenticated users can search profiles
    public ResponseEntity<List<Profile>> searchProfiles(@ModelAttribute ProfileSearchDto searchDto) {
        List<Profile> matchingProfiles = profileService.searchProfiles(searchDto);
        // Nullify AppUser objects in all matching profiles before returning for security
        List<Profile> profilesWithoutAppUser = matchingProfiles.stream()
                .peek(profile -> profile.setAppUser(null))
                .collect(Collectors.toList());
        return new ResponseEntity<>(profilesWithoutAppUser, HttpStatus.OK);
    }

    /**
     * Endpoint to generate and download PDF biodata for a specific profile.
     * Accessible by ADMINs or the owner of the profile.
     * GET /api/profiles/{id}/biodata/pdf
     *
     * @param id The ID of the profile.
     * @return ResponseEntity containing the PDF file.
     */
    @GetMapping("/{id}/biodata/pdf")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<byte[]> generateBiodataPdf(@PathVariable Long id) {
        Optional<Profile> profileOptional = profileService.getProfileById(id);

        if (profileOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Profile profile = profileOptional.get();

        try {
            ByteArrayOutputStream pdfStream = biodataPdfService.generateBiodataPdf(profile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "biodata_" + profile.getFullName().replaceAll("\\s+", "_") + ".pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            // Log the exception for debugging
            e.printStackTrace(); // It's good practice to log the full stack trace for server-side errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
