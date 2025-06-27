// src/main/java/com/marriagebureau/usermanagement/service/UserService.java
package com.marriagebureau.usermanagement.service; // Make sure this package matches your file path

import java.util.List; // Make sure this import path matches
import java.util.Optional; // Make sure this import path matches

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marriagebureau.usermanagement.model.User;
import com.marriagebureau.usermanagement.repository.UserRepository; // Added for findById

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Injected BCryptPasswordEncoder

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to register a new user (renamed from registerNewUser to registerUser for consistency)
    public User registerUser(User user) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' is already registered.");
        }

        // 2. Hash the password before saving! (CRITICAL SECURITY STEP)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Save the user
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // You can add updateUser, deleteUser methods here later.
}