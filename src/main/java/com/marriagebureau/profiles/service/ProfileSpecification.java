// src/main/java/com/marriagebureau/profiles/service/ProfileSpecification.java
package com.marriagebureau.profiles.service;

import com.marriagebureau.profiles.dto.ProfileSearchRequest;
import com.marriagebureau.usermanagement.entity.Profile;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {

    public static Specification<Profile> searchProfiles(ProfileSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Age range
            if (request.getMinAge() != null) {
                // Calculate minimum birth date for max age (e.g., max age 30 means birth year current year - 30)
                // This is an approximation and might need refinement for exact date calculations.
                // A more accurate way would be to calculate the birth date range.
                // For simplicity, we'll compare against the calculated age in the getter,
                // but for true DB queries, it's better to calculate birthdate range:
                // minAge -> birthDate <= today - minAge years
                // maxAge -> birthDate >= today - maxAge years
                // Let's use the actual dateOfBirth for precise filtering in the DB
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"),
                        java.time.LocalDate.now().minusYears(request.getMinAge())));
            }
            if (request.getMaxAge() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"),
                        java.time.LocalDate.now().minusYears(request.getMaxAge() + 1).plusDays(1))); // +1 day to include the maxAge year fully
            }

            // Gender
            if (StringUtils.hasText(request.getGender())) {
                try {
                    Profile.Gender genderEnum = Profile.Gender.valueOf(request.getGender().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("gender"), genderEnum));
                } catch (IllegalArgumentException e) {
                    // Log or handle invalid gender input
                    // For a robust API, consider throwing a custom validation exception or ignoring this criterion.
                }
            }

            // Location
            if (StringUtils.hasText(request.getCountry())) {
                predicates.add(criteriaBuilder.equal(root.get("country"), request.getCountry()));
            }
            if (StringUtils.hasText(request.getState())) {
                predicates.add(criteriaBuilder.equal(root.get("state"), request.getState()));
            }
            if (StringUtils.hasText(request.getCity())) {
                predicates.add(criteriaBuilder.equal(root.get("city"), request.getCity()));
            }

            // Cultural/Background
            if (StringUtils.hasText(request.getReligion())) {
                predicates.add(criteriaBuilder.equal(root.get("religion"), request.getReligion()));
            }
            if (StringUtils.hasText(request.getCaste())) {
                predicates.add(criteriaBuilder.equal(root.get("caste"), request.getCaste()));
            }
            if (StringUtils.hasText(request.getSubCaste())) {
                predicates.add(criteriaBuilder.equal(root.get("subCaste"), request.getSubCaste()));
            }

            // Professional/Educational
            if (StringUtils.hasText(request.getEducationLevel())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("education")), "%" + request.getEducationLevel().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(request.getProfession())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("occupation")), "%" + request.getProfession().toLowerCase() + "%"));
            }

            // Marital Status
            if (StringUtils.hasText(request.getMaritalStatus())) {
                try {
                    Profile.MaritalStatus maritalStatusEnum = Profile.MaritalStatus.valueOf(request.getMaritalStatus().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("maritalStatus"), maritalStatusEnum));
                } catch (IllegalArgumentException e) {
                    // Handle invalid marital status
                }
            }

            // Height range
            if (request.getMinHeightCm() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("heightCm"), request.getMinHeightCm()));
            }
            if (request.getMaxHeightCm() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("heightCm"), request.getMaxHeightCm()));
            }

            // Mother Tongue
            if (StringUtils.hasText(request.getMotherTongue())) {
                try {
                    Profile.MotherTongue motherTongueEnum = Profile.MotherTongue.valueOf(request.getMotherTongue().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("motherTongue"), motherTongueEnum));
                } catch (IllegalArgumentException e) {
                    // Handle invalid mother tongue
                }
            }

            // Lifestyle Habits
            if (StringUtils.hasText(request.getDiet())) {
                try {
                    Profile.Diet dietEnum = Profile.Diet.valueOf(request.getDiet().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("diet"), dietEnum));
                } catch (IllegalArgumentException e) {
                    // Handle invalid diet
                }
            }
            if (StringUtils.hasText(request.getSmokingHabit())) {
                try {
                    Profile.SmokingHabit smokingHabitEnum = Profile.SmokingHabit.valueOf(request.getSmokingHabit().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("smokingHabit"), smokingHabitEnum));
                } catch (IllegalArgumentException e) {
                    // Handle invalid smoking habit
                }
            }
            if (StringUtils.hasText(request.getDrinkingHabit())) {
                try {
                    Profile.DrinkingHabit drinkingHabitEnum = Profile.DrinkingHabit.valueOf(request.getDrinkingHabit().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("drinkingHabit"), drinkingHabitEnum));
                } catch (IllegalArgumentException e) {
                    // Handle invalid drinking habit
                }
            }

            // Only return active profiles
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}