// src/main/java/com/marriagebureau/usermanagement/service/AuthService.java
package com.marriagebureau.usermanagement.service;

// REMOVE: import com.marriagebureau.security.JwtService;
import com.marriagebureau.security.JwtTokenProvider; // <--- This should be the ONLY JWT import

import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.model.Role;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.usermanagement.exception.UserAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider; // <--- Changed to use JwtTokenProvider


    public AuthResponse register(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email '" + registerRequest.getEmail() + "' is already taken.");
        }

        AppUser user = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .build();

        appUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication); // <--- Use jwtTokenProvider

        return new AuthResponse(
                accessToken,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getContactNumber(),
                "Registration successful!"
        );
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateToken(authentication); // <--- Use jwtTokenProvider

        return new AuthResponse(
                accessToken,
                "Bearer",
                authenticatedUser.getId(),
                authenticatedUser.getEmail(),
                authenticatedUser.getRole().name(),
                authenticatedUser.getContactNumber(),
                "Login successful!"
        );
    }

    public AuthResponse registerAdmin(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email '" + registerRequest.getEmail() + "' is already taken.");
        }

        AppUser admin = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .role(Role.ROLE_ADMIN)
                .build();

        appUserRepository.save(admin);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication); // <--- Use jwtTokenProvider

        return new AuthResponse(
                accessToken,
                "Bearer",
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name(),
                admin.getContactNumber(),
                "Admin registration successful!"
        );
    }
}