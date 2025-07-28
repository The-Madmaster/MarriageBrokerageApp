// src/main/java/com/marriagebureau/profiles/repository/ProfileRepository.java
package com.marriagebureau.profiles.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- ADD THIS IMPORT

// Your ProfileRepository should extend both JpaRepository and JpaSpecificationExecutor
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    // Custom query to find a profile by its associated AppUser ID
    // Assuming your Profile entity has a 'broker' field which is the AppUser
    // Adjust this method name if your AppUser relationship field name in Profile is different (e.g., 'user')
    // Corrected method name to match the Profile entity's relationship
    // In Profile.java, the field is `private AppUser appUser;`
    Optional<Profile> findByAppUser_Id(Long appUserId);
}