package com.marriagebureau.usermanagement.repository;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // **IMPORTANT: Add this import**
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// Extend JpaSpecificationExecutor to enable dynamic querying with Specifications
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    // You can keep this custom query if you specifically need it,
    // but often with JpaSpecificationExecutor, complex searches are built dynamically
    // in the service layer using Specifications.
    @Query("SELECT p FROM Profile p WHERE " +
            "(:gender IS NULL OR p.gender = :#{#gender != null ? T(com.marriagebureau.usermanagement.entity.Profile.Gender).valueOf(#gender.toUpperCase()) : null}) AND " + // Handle enum conversion if passing String
            "(:religion IS NULL OR p.religion = :religion) AND " +
            "(:caste IS NULL OR p.caste = :caste) AND " +
            "(:minAge IS NULL OR (FUNCTION('YEAR', CURRENT_DATE()) - FUNCTION('YEAR', p.dateOfBirth)) >= :minAge) AND " +
            "(:maxAge IS NULL OR (FUNCTION('YEAR', CURRENT_DATE()) - FUNCTION('YEAR', p.dateOfBirth)) <= :maxAge) AND " +
            "(:minHeightCm IS NULL OR p.heightCm >= :minHeightCm) AND " +
            "(:maxHeightCm IS NULL OR p.heightCm <= :maxHeightCm) AND " +
            "(:maritalStatus IS NULL OR p.maritalStatus = :#{#maritalStatus != null ? T(com.marriagebureau.usermanagement.entity.Profile.MaritalStatus).valueOf(#maritalStatus.toUpperCase()) : null}) AND " + // Handle enum conversion
            "(:education IS NULL OR p.education = :education) AND " +
            "(:occupation IS NULL OR p.occupation = :occupation) AND " +
            "(:city IS NULL OR p.city = :city) AND " +
            "(:state IS NULL OR p.state = :state) AND " +
            "(:country IS NULL OR p.country = :country) AND " +
            "p.isActive = true" // Always search for active profiles
    )
    List<Profile> findBySearchCriteria(
            @Param("gender") String gender, // Still accepting String, conversion in SpEL
            @Param("religion") String religion,
            @Param("caste") String caste,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("minHeightCm") Integer minHeightCm, // **Changed to Integer**
            @Param("maxHeightCm") Integer maxHeightCm, // **Changed to Integer**
            @Param("maritalStatus") String maritalStatus, // Still accepting String, conversion in SpEL
            @Param("education") String education,
            @Param("occupation") String occupation,
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country
    );

    // This method is for retrieving a profile associated with a specific AppUser object.
    // This is generally the cleaner way if you have the AppUser entity.
    Optional<Profile> findByAppUser(AppUser appUser);

    // If you often need to find a profile just by appUserId without fetching the AppUser object first,
    // you can add this as well.
    Optional<Profile> findByAppUser_Id(Long appUserId); // Spring Data JPA automatically derives this
}