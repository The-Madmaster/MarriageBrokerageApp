// src/main/java/com/marriagebureau/usermanagement/controller/AuthController.java
package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.usermanagement.dto.AuthRequest; // Import AuthRequest
import com.marriagebureau.usermanagement.dto.AuthResponse; // Import AuthResponse
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.service.AppUserService;
import com.marriagebureau.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AppUser appUser) {
        if (appUserService.findByEmail(appUser.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AuthResponse(null, null, "Email already exists!"), HttpStatus.BAD_REQUEST);
        }
        AppUser registeredUser = appUserService.registerNewUser(appUser);
        // After successful registration, you might want to automatically log them in
        // Or just return a success message
        return new ResponseEntity<>(new AuthResponse(null, registeredUser.getId(), "User registered successfully!"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        // Assuming UserPrincipal is used and can expose userId or get AppUser
        Long userId = jwtTokenProvider.getUserIdFromToken(jwt); // Ensure this method exists and works
        if (userId == null) {
            // Fallback or error if userId can't be extracted
            // You might want to get the AppUser from the authentication object if possible
            // For now, let's keep it simple with userId from token
            return new ResponseEntity<>(new AuthResponse(jwt, null, "Login successful, but user ID not found in token!"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new AuthResponse(jwt, userId, "Login successful!"), HttpStatus.OK);
    }
}