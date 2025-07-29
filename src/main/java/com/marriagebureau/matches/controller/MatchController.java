// src/main/java/com/marriagebureau/matches/controller/MatchController.java
package com.marriagebureau.matches.controller;

import com.marriagebureau.matches.dto.MatchResultDto;
import com.marriagebureau.matches.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * Finds potential matches for a given profile ID based on its preferred partner criteria.
     * Accessible by ADMIN, BROKER (to find matches for their clients), and MEMBER (to find matches for themselves).
     * @param profileId The ID of the profile for which to find matches.
     * @return A list of MatchResultDto containing compatible profiles.
     */
    @GetMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BROKER', 'MEMBER')")
    public ResponseEntity<List<MatchResultDto>> getMatchesForProfile(@PathVariable Long profileId) {
        List<MatchResultDto> matches = matchService.findMatchesForProfile(profileId);
        return ResponseEntity.ok(matches);
    }
}