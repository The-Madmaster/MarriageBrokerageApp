package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUser> registerUser(@RequestBody AppUser user) {
        // Lombok's getters are used here (e.g., user.getUsername())
        if (appUserService.findByUsername(user.getUsername()).isPresent() ||
            appUserService.findByEmail(user.getEmail()).isPresent() ||
            appUserService.findByContactNumber(user.getContactNumber()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AppUser newUser = appUserService.createUser(user);
        newUser.setPassword(null); // Nullify password before returning to client for security (Lombok's setPassword)
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return appUserService.findById(id)
                .map(user -> {
                    user.setPassword(null); // Lombok's setPassword
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.findAllUsers();
        List<AppUser> usersWithoutPasswords = users.stream()
                .peek(user -> user.setPassword(null)) // Lombok's setPassword
                .collect(Collectors.toList());
        return new ResponseEntity<>(usersWithoutPasswords, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser userDetails) {
        return appUserService.updateUser(id, userDetails)
                .map(updatedUser -> {
                    updatedUser.setPassword(null); // Lombok's setPassword
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (appUserService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}