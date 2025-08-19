package com.marriagebureau.admin.service;

import com.marriagebureau.admin.dto.AdminDashboardStatsDTO;
import com.marriagebureau.interest.model.InterestStatus;
import com.marriagebureau.interest.repository.InterestRepository;
import com.marriagebureau.clientmanagement.repository.ProfileRepository;
import com.marriagebureau.usermanagement.model.Role;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AppUserRepository appUserRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDTO getDashboardStats() {
        long brokerCount = appUserRepository.countByRole(Role.ROLE_BROKER);
        long profileCount = profileRepository.count();
        long totalInterests = interestRepository.count();
        long acceptedInterests = interestRepository.countByStatus(InterestStatus.ACCEPTED);
        long rejectedInterests = interestRepository.countByStatus(InterestStatus.REJECTED);
        long pendingInterests = interestRepository.countByStatus(InterestStatus.PENDING);

        return AdminDashboardStatsDTO.builder()
                .totalBrokers(brokerCount)
                .totalClientProfiles(profileCount)
                .totalInterestsSent(totalInterests)
                .interestsAccepted(acceptedInterests)
                .interestsRejected(rejectedInterests)
                .interestsPending(pendingInterests)
                .build();
    }
}