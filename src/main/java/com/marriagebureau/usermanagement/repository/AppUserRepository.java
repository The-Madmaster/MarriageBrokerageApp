package com.marriagebureau.usermanagement.repository;

import com.marriagebureau.usermanagement.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Change this method to query by 'email' as it's the actual username field
    Optional<AppUser> findByEmail(String email);

    // If you need to check existence by email
    boolean existsByEmail(String email);

    // If you had other methods like findByUsername, remove or rename them.
}