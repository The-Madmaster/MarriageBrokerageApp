package com.marriagebureau.usermanagement.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.marriagebureau.usermanagement.entity.AppUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p FROM Profile p WHERE " +
            "(:gender IS NULL OR p.gender = :gender) AND " +
            "(:religion IS NULL OR p.religion = :religion) AND " +
            "(:caste IS NULL OR p.caste = :caste) AND " +
            "(:minAge IS NULL OR (FUNCTION('YEAR', CURRENT_DATE()) - FUNCTION('YEAR', p.dateOfBirth)) >= :minAge) AND " +
            "(:maxAge IS NULL OR (FUNCTION('YEAR', CURRENT_DATE()) - FUNCTION('YEAR', p.dateOfBirth)) <= :maxAge) AND " +
            "(:minHeightCm IS NULL OR p.heightCm >= :minHeightCm) AND " + // Use heightCm
            "(:maxHeightCm IS NULL OR p.heightCm <= :maxHeightCm) AND " + // Use heightCm
            "(:maritalStatus IS NULL OR p.maritalStatus = :maritalStatus) AND " + // Added
            "(:education IS NULL OR p.education = :education) AND " +
            "(:occupation IS NULL OR p.occupation = :occupation) AND " +
            "(:city IS NULL OR p.city = :city) AND " +
            "(:state IS NULL OR p.state = :state) AND " +
            "(:country IS NULL OR p.country = :country)"
    )
    List<Profile> findBySearchCriteria(
            @Param("gender") String gender,
            @Param("religion") String religion,
            @Param("caste") String caste,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("minHeightCm") Double minHeightCm, // Changed from minHeight
            @Param("maxHeightCm") Double maxHeightCm, // Changed from maxHeight
            @Param("maritalStatus") String maritalStatus, // Added
            @Param("education") String education,
            @Param("occupation") String occupation,
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country
    );

    // This method might not be needed if appUser is the primary relationship
    // Consider if findByAppUserId (if AppUser has id) or findByAppUser (if you pass AppUser object) is better
    // For now, let's keep it as is, but be mindful of its usage.
    Optional<Profile> findByAppUser(AppUser appUser); // If you're searching by the AppUser object
    // OR
    // Optional<Profile> findByAppUserId(Long appUserId); // If you're searching by the AppUser's ID
}