// src/main/java/com/marriagebureau/brokermanagement/service/BrokerProfileService.java
package com.marriagebureau.brokermanagement.service;

import com.marriagebureau.brokermanagement.dto.BrokerProfileResponse;
import com.marriagebureau.brokermanagement.dto.CreateBrokerProfileRequest;
import com.marriagebureau.brokermanagement.dto.UpdateBrokerProfileRequest; // <-- Ensure this import is present
import com.marriagebureau.brokermanagement.model.BrokerProfile;
import com.marriagebureau.brokermanagement.repository.BrokerProfileRepository;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional; // <-- Ensure this import is present for Optional.ofNullable

@Service
@RequiredArgsConstructor
@Transactional
public class BrokerProfileService {

    private final BrokerProfileRepository brokerProfileRepository;
    private final AppUserRepository appUserRepository;

    /**
     * Creates a new broker profile for a specific AppUser.
     * @param appUserId The ID of the AppUser (broker) for whom the profile is being created.
     * @param request The DTO containing the profile details.
     * @return The created broker profile as a response DTO.
     * @throws ResourceNotFoundException if the AppUser does not exist.
     * @throws IllegalArgumentException if a profile already exists for the given AppUser.
     */
    public BrokerProfileResponse createBrokerProfile(Long appUserId, CreateBrokerProfileRequest request) {
        // 1. Find the associated AppUser
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with ID: " + appUserId));

        // 2. Ensure a profile doesn't already exist for this AppUser
        if (brokerProfileRepository.findByBroker(appUser).isPresent()) {
            throw new IllegalArgumentException("Broker profile already exists for AppUser ID: " + appUserId);
        }

        // 3. Map the CreateBrokerProfileRequest DTO to a BrokerProfile entity
        BrokerProfile brokerProfile = BrokerProfile.builder()
                .broker(appUser) // Link the profile to the AppUser
                .brokerName(request.getBrokerName())
                .firmName(request.getFirmName())
                .registrationNumber(request.getRegistrationNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .firmContactNumber(request.getFirmContactNumber())
                .build();

        // 4. Save the new broker profile to the database
        BrokerProfile savedProfile = brokerProfileRepository.save(brokerProfile);

        // 5. Convert the saved entity back to a BrokerProfileResponse DTO
        return mapToBrokerProfileResponse(savedProfile);
    }

    /**
     * Retrieves a broker profile by the ID of its associated AppUser.
     * @param appUserId The ID of the AppUser whose profile is to be retrieved.
     * @return The broker profile as a response DTO.
     * @throws ResourceNotFoundException if the AppUser or its associated profile is not found.
     */
    public BrokerProfileResponse getBrokerProfileByAppUserId(Long appUserId) {
        // Find the AppUser first (optional but ensures user existence before looking for profile)
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with ID: " + appUserId));

        BrokerProfile brokerProfile = brokerProfileRepository.findByBroker(appUser)
                .orElseThrow(() -> new ResourceNotFoundException("Broker profile not found for AppUser ID: " + appUserId));

        return mapToBrokerProfileResponse(brokerProfile);
    }

    /**
     * Retrieves a broker profile by its own ID.
     * @param profileId The ID of the BrokerProfile entity.
     * @return The broker profile as a response DTO.
     * @throws ResourceNotFoundException if the broker profile with the given ID is not found.
     */
    public BrokerProfileResponse getBrokerProfileById(Long profileId) {
        BrokerProfile brokerProfile = brokerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Broker profile not found with ID: " + profileId));

        return mapToBrokerProfileResponse(brokerProfile);
    }

    /**
     * Updates an existing broker profile for a specific AppUser.
     * This method supports partial updates: only fields provided in the request will be updated.
     *
     * @param appUserId The ID of the AppUser (broker) whose profile is being updated.
     * @param request The DTO containing the updated profile details. Null fields are ignored.
     * @return The updated broker profile as a response DTO.
     * @throws ResourceNotFoundException if the AppUser or its associated profile is not found.
     */
    public BrokerProfileResponse updateBrokerProfile(Long appUserId, UpdateBrokerProfileRequest request) {
        // 1. Find the AppUser to ensure it exists
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with ID: " + appUserId));

        // 2. Find the existing BrokerProfile associated with this AppUser
        BrokerProfile existingProfile = brokerProfileRepository.findByBroker(appUser)
                .orElseThrow(() -> new ResourceNotFoundException("Broker profile not found for AppUser ID: " + appUserId));

        // 3. Update fields only if they are provided (not null) in the request DTO
        // This allows for partial updates.
        Optional.ofNullable(request.getBrokerName()).ifPresent(existingProfile::setBrokerName);
        Optional.ofNullable(request.getFirmName()).ifPresent(existingProfile::setFirmName);
        Optional.ofNullable(request.getRegistrationNumber()).ifPresent(existingProfile::setRegistrationNumber);
        Optional.ofNullable(request.getAddress()).ifPresent(existingProfile::setAddress);
        Optional.ofNullable(request.getCity()).ifPresent(existingProfile::setCity);
        Optional.ofNullable(request.getState()).ifPresent(existingProfile::setState);
        Optional.ofNullable(request.getPincode()).ifPresent(existingProfile::setPincode);
        Optional.ofNullable(request.getFirmContactNumber()).ifPresent(existingProfile::setFirmContactNumber);

        // 4. Save the modified entity. The @Transactional annotation will ensure the changes are committed.
        BrokerProfile updatedProfile = brokerProfileRepository.save(existingProfile);

        // 5. Convert the updated entity back to a response DTO and return
        return mapToBrokerProfileResponse(updatedProfile);
    }

    /**
     * Deletes a broker profile associated with a specific AppUser.
     *
     * @param appUserId The ID of the AppUser (broker) whose profile is to be deleted.
     * @throws ResourceNotFoundException if the AppUser or its associated profile is not found.
     */
    public void deleteBrokerProfile(Long appUserId) {
        // 1. Find the AppUser to ensure it exists
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with ID: " + appUserId));

        // 2. Find the existing BrokerProfile associated with this AppUser
        BrokerProfile existingProfile = brokerProfileRepository.findByBroker(appUser)
                .orElseThrow(() -> new ResourceNotFoundException("Broker profile not found for AppUser ID: " + appUserId));

        // 3. Crucial: Sever the bidirectional link from the AppUser (non-owning side)
        // This helps maintain data consistency in the application's object graph
        // and can prevent issues if AppUser is later fetched without its profile.
        if (appUser.getBrokerProfile() != null && appUser.getBrokerProfile().equals(existingProfile)) {
            appUser.setBrokerProfile(null);
            // Save the AppUser to persist the change in its relationship
            appUserRepository.save(appUser);
        }

        // 4. Delete the BrokerProfile entity from the database
        brokerProfileRepository.delete(existingProfile);
    }

    /**
     * Helper method to convert a BrokerProfile entity to a BrokerProfileResponse DTO.
     * @param brokerProfile The BrokerProfile entity to convert.
     * @return The corresponding BrokerProfileResponse DTO.
     */
    private BrokerProfileResponse mapToBrokerProfileResponse(BrokerProfile brokerProfile) {
        BrokerProfileResponse response = new BrokerProfileResponse();
        response.setId(brokerProfile.getId());
        response.setAppUserId(brokerProfile.getBroker().getId()); // Get the AppUser's ID
        response.setBrokerName(brokerProfile.getBrokerName());
        response.setFirmName(brokerProfile.getFirmName());
        response.setRegistrationNumber(brokerProfile.getRegistrationNumber());
        response.setAddress(brokerProfile.getAddress());
        response.setCity(brokerProfile.getCity());
        response.setState(brokerProfile.getState());
        response.setPincode(brokerProfile.getPincode());
        response.setFirmContactNumber(brokerProfile.getFirmContactNumber());
        response.setCreatedDate(brokerProfile.getCreatedDate());
        response.setLastUpdatedDate(brokerProfile.getLastUpdatedDate());
        return response;
    }
}