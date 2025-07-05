package com.marriagebureau.usermanagement.service;

import com.marriagebureau.security.JwtTokenProvider;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Keep this import, though not directly used for setting now() here

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }

        AppUser user = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                // Removed .username() as it no longer exists in AppUser
                // Removed .enabled(), .createdDate(), .lastUpdatedDate(), .role()
                // because AppUser now uses @Builder.Default for these fields.
                // If registerRequest has contactNumber, ensure it's provided.
                .contactNumber(registerRequest.getContactNumber())
                .build(); // Let AppUser's @Builder.Default handle the other fields

        appUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        // Make sure AuthResponse has a constructor like AuthResponse(String accessToken, Long userId, String username, String email)
        // Note: user.getUsername() will now return the email, as per AppUser's getUsername() implementation.
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(authentication);

        // Make sure AuthResponse has a constructor like AuthResponse(String accessToken, Long userId, String username, String email)
        return new AuthResponse(token, authenticatedUser.getId(), authenticatedUser.getUsername(), authenticatedUser.getEmail());
    }

    public AuthResponse registerAdmin(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }

        AppUser admin = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                // Removed .username()
                // Removed .enabled(), .createdDate(), .lastUpdatedDate()
                .contactNumber(registerRequest.getContactNumber())
                .role(AppUser.Role.ROLE_ADMIN) // Explicitly set admin role
                .build(); // Let AppUser's @Builder.Default handle other fields

        appUserRepository.save(admin);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        // Make sure AuthResponse has a constructor like AuthResponse(String accessToken, Long userId, String username, String email)
        return new AuthResponse(token, admin.getId(), admin.getUsername(), admin.getEmail());
    }
}