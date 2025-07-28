// src/main/java/com/marriagebureau/brokermanagement/controller/BrokerProfileController.java
package com.marriagebureau.brokermanagement.controller;

import com.marriagebureau.brokermanagement.dto.BrokerProfileResponse;
import com.marriagebureau.brokermanagement.dto.CreateBrokerProfileRequest;
import com.marriagebureau.brokermanagement.dto.UpdateBrokerProfileRequest; // Import for update DTO
import com.marriagebureau.brokermanagement.service.BrokerProfileService;
import com.marriagebureau.usermanagement.model.AppUser; // To get the authenticated AppUser
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;

import jakarta.validation.Valid; // For DTO validation
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // For role-based access control
import org.springframework.security.core.annotation.AuthenticationPrincipal; // To inject authenticated user details
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/broker-profiles") // Base URL for broker profile endpoints
@RequiredArgsConstructor // Injects BrokerProfileService
public class BrokerProfileController {

    private final BrokerProfileService brokerProfileService;

    /**
     * Endpoint for an authenticated BROKER to create their own profile.
     * Each broker can only have one profile.
     * Requires ROLE_BROKER.
     * POST /api/broker-profiles/me
     */
    @PostMapping("/me")
    @PreAuthorize("hasRole('BROKER')")
    public ResponseEntity<BrokerProfileResponse> createMyBrokerProfile(
            @Valid @RequestBody CreateBrokerProfileRequest request,
            @AuthenticationPrincipal AppUser appUser) { // AppUser is injected from the security context
        BrokerProfileResponse response = brokerProfileService.createBrokerProfile(appUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint for an authenticated BROKER to retrieve their own profile.
     * Requires ROLE_BROKER.
     * GET /api/broker-profiles/me
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('BROKER')")
    public ResponseEntity<BrokerProfileResponse> getMyBrokerProfile(
            @AuthenticationPrincipal AppUser appUser) {
        BrokerProfileResponse response = brokerProfileService.getBrokerProfileByAppUserId(appUser.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an authenticated BROKER to update their own profile.
     * Requires ROLE_BROKER.
     * PUT /api/broker-profiles/me
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('BROKER')")
    public ResponseEntity<BrokerProfileResponse> updateMyBrokerProfile(
            @Valid @RequestBody UpdateBrokerProfileRequest request,
            @AuthenticationPrincipal AppUser appUser) {
        BrokerProfileResponse response = brokerProfileService.updateBrokerProfile(appUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an authenticated BROKER to delete their own profile.
     * Requires ROLE_BROKER.
     * DELETE /api/broker-profiles/me
     */
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('BROKER')")
    public ResponseEntity<Void> deleteMyBrokerProfile(@AuthenticationPrincipal AppUser appUser) {
        brokerProfileService.deleteBrokerProfile(appUser.getId());
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }

    /**
     * Endpoint for an ADMIN to retrieve a broker profile by the associated AppUser's ID.
     * Requires ROLE_ADMIN.
     * GET /api/broker-profiles/by-user/{appUserId}
     */
    @GetMapping("/by-user/{appUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrokerProfileResponse> getBrokerProfileByAppUserId(@PathVariable Long appUserId) {
        BrokerProfileResponse response = brokerProfileService.getBrokerProfileByAppUserId(appUserId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an ADMIN to retrieve a broker profile by its own BrokerProfile ID.
     * Requires ROLE_ADMIN.
     * GET /api/broker-profiles/{profileId}
     */
    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrokerProfileResponse> getBrokerProfileById(@PathVariable Long profileId) {
        BrokerProfileResponse response = brokerProfileService.getBrokerProfileById(profileId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an ADMIN to update any broker's profile by its BrokerProfile ID.
     * This method fetches the associated AppUser ID and uses the existing service method.
     * Requires ROLE_ADMIN.
     * PUT /api/broker-profiles/{profileId}
     */
    @PutMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrokerProfileResponse> updateBrokerProfileById(
            @PathVariable Long profileId,
            @Valid @RequestBody UpdateBrokerProfileRequest request) {
        // First, get the AppUser ID associated with this profile ID
        // This leverages the getBrokerProfileById service method
        Long appUserId = brokerProfileService.getBrokerProfileById(profileId).getAppUserId();
        BrokerProfileResponse response = brokerProfileService.updateBrokerProfile(appUserId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an ADMIN to delete any broker's profile by its BrokerProfile ID.
     * This method fetches the associated AppUser ID and uses the existing service method.
     * Requires ROLE_ADMIN.
     * DELETE /api/broker-profiles/{profileId}
     */
    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBrokerProfileById(@PathVariable Long profileId) {
        // First, get the AppUser ID associated with this profile ID
        Long appUserId = brokerProfileService.getBrokerProfileById(profileId).getAppUserId();
        brokerProfileService.deleteBrokerProfile(appUserId);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }
}