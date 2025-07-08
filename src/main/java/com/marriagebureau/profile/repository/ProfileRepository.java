package com.marriagebureau.profile.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Corrected method name to reflect the 'appUser' property in the Profile entity
    Optional<Profile> findByAppUserId(Long appUserId);
}