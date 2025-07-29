// src/main/java/com/marriagebureau/matches/dto/MatchResultDto.java
package com.marriagebureau.matches.dto;

import com.marriagebureau.profiles.dto.ProfileResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResultDto {
    private ProfileResponse matchedProfile;
    private double compatibilityScore; // A score between 0.0 and 1.0 (or 0-100) indicating compatibility
}