package com.marriagebureau.usermanagement.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Corrected to use findByAppUser_Id as per JPA naming conventions for OneToOne relationships
    Optional<Profile> findByAppUser_Id(Long appUserId);

    // Ensure all parameters that were Gender/MaritalStatus are now String
    @Query("SELECT p FROM Profile p WHERE " +
           "(:gender IS NULL OR p.gender = :gender) AND " +
           "(:minAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', p.dateOfBirth) >= :minAge) AND " +
           "(:maxAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', p.dateOfBirth) <= :maxAge) AND " +
           "(:religion IS NULL OR p.religion = :religion) AND " +
           "(:caste IS NULL OR p.caste = :caste) AND " +
           "(:maritalStatus IS NULL OR p.maritalStatus = :maritalStatus) AND " +
           "(:minHeightCm IS NULL OR p.heightCm >= :minHeightCm) AND " +
           "(:maxHeightCm IS NULL OR p.heightCm <= :maxHeightCm) AND " +
           "(:city IS NULL OR p.city = :city) AND " +
           "(:state IS NULL OR p.state = :state) AND " +
           "(:country IS NULL OR p.country = :country) AND " +
           "(:education IS NULL OR p.education = :education) AND " +
           "(:occupation IS NULL OR p.occupation = :occupation)")
    List<Profile> findBySearchCriteria( // Changed method name for clarity, ensure service calls this
            @Param("gender") String gender, // Make sure this is String
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("religion") String religion,
            @Param("caste") String caste,
            @Param("maritalStatus") String maritalStatus, // Make sure this is String
            @Param("minHeightCm") Double minHeightCm,
            @Param("maxHeightCm") Double maxHeightCm,
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country,
            @Param("education") String education,
            @Param("occupation") String occupation
    );
}