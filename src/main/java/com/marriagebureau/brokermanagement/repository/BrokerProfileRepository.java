// src/main/java/com/marriagebureau/brokermanagement/repository/BrokerProfileRepository.java
package com.marriagebureau.brokermanagement.repository;

import com.marriagebureau.brokermanagement.model.BrokerProfile; // Import the BrokerProfile entity
import com.marriagebureau.usermanagement.model.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrokerProfileRepository extends JpaRepository<BrokerProfile, Long> {

    // Custom method to find a BrokerProfile by its associated AppUser
    // This is useful when a broker logs in and wants to retrieve their profile.
    Optional<BrokerProfile> findByBroker(AppUser broker);

    // Optional: Find by AppUser ID if you prefer not to pass the whole AppUser object
    Optional<BrokerProfile> findByBroker_Id(Long appUserId);
}