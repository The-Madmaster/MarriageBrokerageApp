package com.marriagebureau.usermanagement.service;

import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder; // Needed for encoding passwords

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder; // Initialize PasswordEncoder
    }

    // Method for registering a new user (as called by AuthController)
    public AppUser registerNewUser(AppUser appUser) {
        // Encode the password before saving
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        // You might want to set default roles or other initializations here
        return appUserRepository.save(appUser);
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findUserById(Long id) {
        return appUserRepository.findById(id);
    }

    @Transactional
    public AppUser updateUser(Long id, AppUser updatedAppUser) {
        return appUserRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(updatedAppUser.getEmail());
                    // Only update password if it's explicitly set and needs re-encoding
                    if (updatedAppUser.getPassword() != null && !updatedAppUser.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(updatedAppUser.getPassword()));
                    }
                    existingUser.setRole(updatedAppUser.getRole());
                    // Update other fields as necessary from updatedAppUser
                    return appUserRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}