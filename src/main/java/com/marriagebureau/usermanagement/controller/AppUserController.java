package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.entity.AppUser;
import com.marriagebureau.usermanagement.service.AppUserService; // Correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService; // Corrected variable name to match the convention (lowercase first letter)

    @Autowired
    public AppUserController(AppUserService appUserService) { // Parameter name is also corrected
        this.appUserService = appUserService;
    }

    /**
     * Registers a new user.
     * POST /api/users
     * @param user The user object to register.
     * @return ResponseEntity with the created user (password nullified) and HTTP status.
     */
    @PostMapping
    public ResponseEntity<AppUser> registerUser(@RequestBody AppUser user) {
        if (appUserService.findByUsername(user.getUsername()).isPresent()) { // Changed userService to appUserService
            return new ResponseEntity<>(HttpStatus.CONFLICT); // User already exists
        }
        AppUser newUser = appUserService.createUser(user); // Changed userService to appUserService
        // Important: Nullify password before returning to client for security
        newUser.setPassword(null);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by their ID.
     * GET /api/users/{id}
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user (password nullified) and HTTP status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return appUserService.findById(id) // Changed userService to appUserService
                .map(user -> {
                    user.setPassword(null); // Nullify password
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all registered users.
     * GET /api/users
     * @return ResponseEntity with a list of all users (passwords nullified) and HTTP status.
     */
    @GetMapping // This is important
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.findAllUsers(); // Changed userService to appUserService
        // Nullify passwords for all users before returning
        List<AppUser> usersWithoutPasswords = users.stream()
                .peek(user -> user.setPassword(null))
                .collect(Collectors.toList());
        return new ResponseEntity<>(usersWithoutPasswords, HttpStatus.OK);
    }

    /**
     * Updates an existing user's information.
     * PUT /api/users/{id}
     * @param id The ID of the user to update.
     * @param userDetails The user object with updated information.
     * @return ResponseEntity with the updated user (password nullified) and HTTP status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser userDetails) {
        return appUserService.updateUser(id, userDetails) // Changed userService to appUserService
                .map(updatedUser -> {
                    updatedUser.setPassword(null); // Nullify password
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a user by their ID.
     * DELETE /api/users/{id}
     * @param id The ID of the user to delete.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (appUserService.deleteUser(id)) { // Changed userService to appUserService
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
    }
}