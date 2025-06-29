package com.marriagebureau.repository;

import com.marriagebureau.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // You might want to add custom query methods here later,
    // for example, to find a profile by email or user ID.
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByCreatedByUser_Id(Long userId); // Assuming 'createdByUser' is the field in Profile linking to User

}