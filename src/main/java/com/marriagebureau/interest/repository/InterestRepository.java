package com.marriagebureau.interest.repository;

import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.interest.model.Interest;
import com.marriagebureau.interest.model.InterestStatus; // Import InterestStatus
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findBySenderProfileId(Long senderProfileId);

    List<Interest> findByReceiverProfileId(Long receiverProfileId);

    boolean existsBySenderProfileAndReceiverProfile(Profile senderProfile, Profile receiverProfile);

    // --- ADD THIS NEW METHOD ---
    /**
     * Counts the number of interests with a specific status.
     * @param status The InterestStatus to count.
     * @return The total count of interests with that status.
     */
    long countByStatus(InterestStatus status);
}