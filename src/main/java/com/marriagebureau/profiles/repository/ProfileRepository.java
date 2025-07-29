// src/main/java/com/marriagebureau/profiles/repository/ProfileRepository.java
package com.marriagebureau.profiles.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.marriagebureau.profiles.dto.UpdateProfileRequest;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByAppUserId(Long appUserId);

    // Custom query to find all active profiles excluding a given ID
    @Query("SELECT p FROM Profile p WHERE p.isActive = true AND p.id <> :excludeProfileId")
    List<Profile> findAllActiveProfilesExcluding(@Param("excludeProfileId") Long excludeProfileId);
}