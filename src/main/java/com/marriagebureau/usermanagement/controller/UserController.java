// UserController.java
package com.marriagebureau.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Added for GET endpoint
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marriagebureau.usermanagement.model.User;
import com.marriagebureau.usermanagement.service.UserService; // Added for List<User>

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // Return a simplified user object, excluding password for security
            User responseUser = new User();
            responseUser.setId(registeredUser.getId());
            responseUser.setName(registeredUser.getName());
            responseUser.setEmail(registeredUser.getEmail());
            responseUser.setContactNumber(registeredUser.getContactNumber());
            responseUser.setRole(registeredUser.getRole());

            return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
        } catch (Exception e) {
            return new ResponseEntity<>("User registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    /**
     * Endpoint to get all registered users.
     * Example Request (GET /api/users):
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // In a real application, you might want to map these to DTOs
        // to exclude sensitive information like passwords.
        // For now, it will return the full User object (except hashed password from DB)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // We will add more endpoints here for login, get user details, etc., later.
}