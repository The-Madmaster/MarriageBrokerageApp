package com.marriagebureau.usermanagement.repository; // Corrected package name

import com.marriagebureau.entity.AppProfile; // Changed from Profile to AppProfile
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<AppProfile, Long> { // Changed from Profile to AppProfile

    // You might want to add custom query methods here later,
    // for example, to find a profile by email or user ID.
    Optional<AppProfile> findByEmail(String email); // Changed return type to AppProfile
    Optional<AppProfile> findByCreatedByUser_Id(Long userId); // Changed return type to AppProfile (and method looks good)

}