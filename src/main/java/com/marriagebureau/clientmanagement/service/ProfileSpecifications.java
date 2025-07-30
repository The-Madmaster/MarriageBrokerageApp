package com.marriagebureau.clientmanagement.service;

import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.clientmanagement.model.enums.Gender;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDate;

public class ProfileSpecifications {

    public static Specification<Profile> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Profile> hasMinAge(Integer minAge) {
        if (minAge == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dateOfBirth"), LocalDate.now().minusYears(minAge));
    }

    public static Specification<Profile> hasMaxAge(Integer maxAge) {
        if (maxAge == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateOfBirth"), LocalDate.now().minusYears(maxAge + 1).plusDays(1));
    }

    public static Specification<Profile> hasGender(String gender) {
        if (!StringUtils.hasText(gender)) return null;
        try {
            return (root, query, cb) -> cb.equal(root.get("gender"), Gender.valueOf(gender.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null; // Ignore invalid gender criteria
        }
    }

    public static Specification<Profile> hasReligion(String religion) {
        if (!StringUtils.hasText(religion)) return null;
        return (root, query, cb) -> cb.equal(root.get("religion"), religion);
    }
    
    public static Specification<Profile> hasCountry(String country) {
        if (!StringUtils.hasText(country)) return null;
        return (root, query, cb) -> cb.equal(root.get("country"), country);
    }
}