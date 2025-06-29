package com.marriagebureau.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marriagebureau.entity.AppUser; // Correct import for AppUser

@Repository // Marks this interface as a Spring Data JPA repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // JpaRepository provides standard CRUD operations like save(), findById(), findAll(), delete()

    // Custom method to find a user by their email. Spring Data JPA automatically implements this!
    Optional<AppUser> findByEmail(String email); // Corrected to AppUser

    Optional<AppUser> findByUsername(String username);
    // Custom method to find a user by their contact number
    Optional<AppUser> findByContactNumber(String contactNumber);
}