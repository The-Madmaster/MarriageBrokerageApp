package com.marriagebureau.clientmanagement.controller;

import com.marriagebureau.clientmanagement.dto.CreateProfileRequest;
import com.marriagebureau.clientmanagement.dto.ProfileResponse;
import com.marriagebureau.clientmanagement.dto.UpdateProfileRequest;
import com.marriagebureau.clientmanagement.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ProfileService profileService;

    /**
     * Endpoint for a broker to create a new client profile.
     * The profile is automatically associated with the authenticated broker.
     *
     * @param request The request body containing the client's profile details.
     * @return The created profile data with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<ProfileResponse> createClientProfile(@Valid @RequestBody CreateProfileRequest request) {
        ProfileResponse createdProfile = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    /**
     * Endpoint to retrieve all client profiles associated with the authenticated broker.
     *
     * @return A list of client profiles.
     */
    @GetMapping
    public ResponseEntity<List<ProfileResponse>> getAuthenticatedBrokerClients() {
        List<ProfileResponse> profiles = profileService.getClientsForAuthenticatedBroker();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Endpoint to retrieve a specific client profile by its ID.
     * It ensures the profile belongs to a client of the authenticated broker.
     *
     * @param id The unique identifier of the client profile.
     * @return The client profile data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getClientProfileById(@PathVariable Long id) {
        ProfileResponse profile = profileService.getProfileByIdForBroker(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * Endpoint to update an existing client profile.
     * It ensures the profile belongs to a client of the authenticated broker.
     *
     * @param id The unique identifier of the client profile to update.
     * @param request The request body with the updated profile details.
     * @return The updated client profile data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateClientProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest request) {
        ProfileResponse updatedProfile = profileService.updateProfile(id, request);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Endpoint to delete a client profile.
     * It ensures the profile belongs to a client of the authenticated broker.
     *
     * @param id The unique identifier of the client profile to delete.
     * @return A 204 No Content status on successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}