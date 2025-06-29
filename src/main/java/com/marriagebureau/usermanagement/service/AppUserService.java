// src/main/java/com/marriagebureau/usermanagement/service/AppUserService.java

package com.marriagebureau.usermanagement.service;

import com.marriagebureau.entity.AppUser; // IMPORTANT: Ensure AppUser is in this package
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.entity.UserRole;// Ensure UserRole is in this package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections; // ADDED: Import for Collections.singleton
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appuserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appuserRepository, PasswordEncoder passwordEncoder) {
        this.appuserRepository = appuserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser createUser(AppUser appUser) {
        // Check for existing user (username, email, contact number)
        if (appuserRepository.findByUsername(appUser.getUsername()).isPresent() ||
            appuserRepository.findByEmail(appUser.getEmail()).isPresent() ||
            appuserRepository.findByContactNumber(appUser.getContactNumber()).isPresent()) {
            throw new IllegalArgumentException("User with this username, email or contact number already exists.");
        }

        // Encode the password before saving
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setEnabled(true); // Default to enabled
        appUser.setCreatedDate(LocalDateTime.now());
        appUser.setLastUpdatedDate(LocalDateTime.now());

        // ADDED: Ensure roles are set, default to USER if not provided or empty
        if (appUser.getRoles() == null || appUser.getRoles().isEmpty()) {
            appUser.setRoles(Collections.singleton(UserRole.USER));
        }

        return this.appuserRepository.save(appUser);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByUsername(String username) {
        return this.appuserRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findById(Long id) {
        return this.appuserRepository.findById(id);
    }

    /**
     * Retrieves all users from the database.
     * @return A list of all users.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllUsers() {
        return this.appuserRepository.findAll();
    }

    /**
     * Updates an existing user's information.
     * This method handles finding the user by ID and then updating its fields.
     *
     * @param id The ID of the user to update.
     * @param updatedUserDetails The AppUser object containing the updated information.
     * Password will only be updated if it's explicitly set in updatedUserDetails.
     * @return The updated AppUser object, or Optional.empty if the user is not found.
     */
    @Transactional
    public Optional<AppUser> updateUser(Long id, AppUser updatedUserDetails) {
        return this.appuserRepository.findById(id).map(existingUser -> {
            // Only update username if provided and different
            if (updatedUserDetails.getUsername() != null && !updatedUserDetails.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUserDetails.getUsername());
            }
            // Only update email if provided and different
            if (updatedUserDetails.getEmail() != null && !updatedUserDetails.getEmail().isEmpty()) {
                existingUser.setEmail(updatedUserDetails.getEmail());
            }
            // Only update contactNumber if provided and different
            if (updatedUserDetails.getContactNumber() != null && !updatedUserDetails.getContactNumber().isEmpty()) {
                existingUser.setContactNumber(updatedUserDetails.getContactNumber());
            }
            // Only update password if provided in the update request
            if (updatedUserDetails.getPassword() != null && !updatedUserDetails.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUserDetails.getPassword()));
            }
            // User roles might need a more sophisticated update strategy (e.g., add/remove specific roles)
            // For simplicity, we'll assume roles are managed elsewhere or replace them if provided entirely
            if (updatedUserDetails.getRoles() != null && !updatedUserDetails.getRoles().isEmpty()) {
                existingUser.setRoles(updatedUserDetails.getRoles());
            }

            // Update last updated date
            existingUser.setLastUpdatedDate(LocalDateTime.now());

            return this.appuserRepository.save(existingUser);
        });
    }

    /**
     * Deletes a user by their ID.
     * @param id The ID of the user to delete.
     * @return true if the user was found and deleted, false otherwise.
     */
    @Transactional
    public boolean deleteUser(Long id) {
        if (this.appuserRepository.existsById(id)) {
            this.appuserRepository.deleteById(id);
            return true;
        }
        return false;
    }
}