package com.marriagebureau.clientmanagement.repository;

import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.usermanagement.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    /**
     * Finds all client profiles associated with a specific broker.
     * This is crucial for retrieving all clients managed by the authenticated broker.
     *
     * @param broker The broker (AppUser) entity.
     * @return A list of client profiles.
     */
    List<Profile> findAllByBroker(AppUser broker);

    /**
     * Finds all active profiles, excluding a specific profile by its ID.
     * This uses Spring Data JPA's derived query feature for a cleaner implementation.
     *
     * @param profileId The ID of the profile to exclude from the results.
     * @return A list of active profiles.
     */
    List<Profile> findAllByIsActiveTrueAndIdNot(Long profileId);
}