package com.marriagebureau.interest.repository;

import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.interest.model.Interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    /**
     * Finds all interests sent by a specific client profile.
     * @param senderProfileId The ID of the sending profile.
     * @return A list of interests.
     */
    List<Interest> findBySenderProfileId(Long senderProfileId);

    /**
     * Finds all interests received by a specific client profile.
     * @param receiverProfileId The ID of the receiving profile.
     * @return A list of interests.
     */
    List<Interest> findByReceiverProfileId(Long receiverProfileId);

    /**
     * Checks if an interest request already exists between a specific sender and receiver.
     * This is used to prevent duplicate interest requests.
     * @param senderProfile The sending profile entity.
     * @param receiverProfile The receiving profile entity.
     * @return true if an interest already exists, false otherwise.
     */
    boolean existsBySenderProfileAndReceiverProfile(Profile senderProfile, Profile receiverProfile);
}