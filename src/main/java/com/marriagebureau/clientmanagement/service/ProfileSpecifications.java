package com.marriagebureau.clientmanagement.service;

import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.clientmanagement.model.enums.Gender;
import com.marriagebureau.clientmanagement.model.enums.MaritalStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDate;

public class ProfileSpecifications {

    public static Specification<Profile> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Profile> isNotProfile(Long profileId) {
        if (profileId == null) return null;
        return (root, query, cb) -> cb.notEqual(root.get("id"), profileId);
    }

    public static Specification<Profile> hasGender(Gender gender) {
        if (gender == null) return null;
        return (root, query, cb) -> cb.equal(root.get("gender"), gender);
    }

    public static Specification<Profile> isWithinAgeRange(Integer minAge, Integer maxAge) {
        Specification<Profile> spec = Specification.where(null);
        if (minAge != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dateOfBirth"), LocalDate.now().minusYears(minAge)));
        }
        if (maxAge != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateOfBirth"), LocalDate.now().minusYears(maxAge + 1).plusDays(1)));
        }
        return spec;
    }
    
    public static Specification<Profile> isWithinHeightRange(Integer minHeight, Integer maxHeight) {
        Specification<Profile> spec = Specification.where(null);
        if (minHeight != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("heightCm"), minHeight));
        }
        if (maxHeight != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("heightCm"), maxHeight));
        }
        return spec;
    }

    public static Specification<Profile> hasReligion(String religion) {
        if (!StringUtils.hasText(religion)) return null;
        return (root, query, cb) -> cb.equal(root.get("religion"), religion);
    }
    
    public static Specification<Profile> hasCaste(String caste) {
        if (!StringUtils.hasText(caste)) return null;
        return (root, query, cb) -> cb.equal(root.get("caste"), caste);
    }

    public static Specification<Profile> hasMaritalStatus(MaritalStatus status) {
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("maritalStatus"), status);
    }
}