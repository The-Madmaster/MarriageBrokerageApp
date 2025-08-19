package com.marriagebureau.usermanagement.repository;

import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.model.Role; // Import Role
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    // --- ADD THIS NEW METHOD ---
    /**
     * Counts the number of users with a specific role.
     * @param role The role to count.
     * @return The total count of users with that role.
     */
    long countByRole(Role role);
}