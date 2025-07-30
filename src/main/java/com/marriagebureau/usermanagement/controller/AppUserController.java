package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/users") // A more specific admin path
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllAppUsers() {
        List<AppUser> users = appUserService.findAllUsers(); // Assuming findAllUsers in service
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable Long id) {
        Optional<AppUser> user = appUserService.findUserById(id); // Assuming findUserById in service
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable Long id, @RequestBody AppUser appUser) {
        AppUser updatedUser = appUserService.updateUser(id, appUser); // Assuming updateUser in service
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        appUserService.deleteUser(id); // Assuming deleteUser in service
        return ResponseEntity.noContent().build();
    }
}