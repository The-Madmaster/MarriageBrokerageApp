package com.marriagebureau.usermanagement.service;

import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser createUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            // FIX: Ensure this line uses ROLE_USER
            user.setRoles(Collections.singletonList(AppUser.Role.ROLE_USER));
        }
        user.setEnabled(true);
        return appUserRepository.save(user);
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> findByContactNumber(String contactNumber) {
        return appUserRepository.findByContactNumber(contactNumber);
    }

    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    @Transactional
    public Optional<AppUser> updateUser(Long id, AppUser userDetails) {
        return appUserRepository.findById(id).map(existingUser -> {
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setContactNumber(userDetails.getContactNumber());
            existingUser.setEnabled(userDetails.isEnabled());
            return appUserRepository.save(existingUser);
        });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (appUserRepository.existsById(id)) {
            appUserRepository.deleteById(id);
            return true;
        }
        return false;
    }
}