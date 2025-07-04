// src/main/java/com/marriagebureau/usermanagement/service/AuthService.java
package com.marriagebureau.usermanagement.service;

import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        // Create new user's account
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER"); // Default role for new registrations
        user.setEnabled(true); // User is enabled by default

        // Save the user
        AppUser savedUser = appUserRepository.save(user);

        // Authenticate the user immediately after registration and generate a token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return new AuthResponse(jwt, savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        AppUser authenticatedUser = appUserRepository.findByEmail(request.getEmail())
                                                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return new AuthResponse(jwt, authenticatedUser.getId(), authenticatedUser.getEmail(), authenticatedUser.getRole());
    }
}