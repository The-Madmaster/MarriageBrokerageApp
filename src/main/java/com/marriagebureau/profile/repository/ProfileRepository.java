// src/main/java/com/marriagebureau/profile/repository/ProfileRepository.java

package com.marriagebureau.profile.repository;

import com.marriagebureau.usermanagement.entity.Profile; // Corrected import for Profile
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Custom method to find a Profile by the associated AppUser's ID
    Optional<Profile> findByAppUserId(Long appUserId);
}