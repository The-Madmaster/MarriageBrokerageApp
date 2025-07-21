package com.marriagebureau.controller;


import com.marriagebureau.dto.*; // Import all your DTOs
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate; // For handling dates if you choose to use it

@RestController // Marks this class as a REST controller
@RequestMapping("/api") // Base path for all endpoints in this controller
public class DashboardController {

    // Endpoint for Dashboard Statistics: /api/dashboard/stats
    @GetMapping("/dashboard/stats")
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setActiveClients(150); // Example data
        stats.setSuccessfulMatches(30); // Example data
        stats.setMonthlyRevenue(95000.00); // Example data
        stats.setAvgMatchScore(82); // Example data
        return stats;
    }

    // Endpoint for Clients Data: /api/clients
    @GetMapping("/clients")
    public List<ClientDTO> getAllClients() {
        List<ClientDTO> clients = new ArrayList<>();

        // Example Data for Clients (replace with actual data from database later)
        ClientDTO client1 = new ClientDTO();
        client1.setId(1L);
        client1.setName("Ravi Kumar");
        client1.setAge(28);
        client1.setImage("https://randomuser.me/api/portraits/men/1.jpg"); // Use placeholder image
        client1.setReligion("Hindu");
        client1.setCaste("Brahmin");
        client1.setOccupation("Software Engineer");
        client1.setStatus("Active");
        client1.setLastContact("2025-07-15");
        client1.setCompatibilityScore(85);
        clients.add(client1);

        ClientDTO client2 = new ClientDTO();
        client2.setId(2L);
        client2.setName("Priya Sharma");
        client2.setAge(26);
        client2.setImage("https://randomuser.me/api/portraits/women/2.jpg"); // Use placeholder image
        client2.setReligion("Hindu");
        client2.setCaste("Rajput");
        client2.setOccupation("Doctor");
        client2.setStatus("Pending");
        client2.setLastContact("2025-07-20");
        client2.setCompatibilityScore(72);
        clients.add(client2);

        ClientDTO client3 = new ClientDTO();
        client3.setId(3L);
        client3.setName("Ahmed Khan");
        client3.setAge(30);
        client3.setImage("https://randomuser.me/api/portraits/men/3.jpg");
        client3.setReligion("Islam");
        client3.setCaste("Sunni");
        client3.setOccupation("Architect");
        client3.setStatus("Active");
        client3.setLastContact("2025-07-18");
        client3.setCompatibilityScore(90);
        clients.add(client3);

        ClientDTO client4 = new ClientDTO();
        client4.setId(4L);
        client4.setName("Sara Ali");
        client4.setAge(27);
        client4.setImage("https://randomuser.me/api/portraits/women/4.jpg");
        client4.setReligion("Islam");
        client4.setCaste("Shia");
        client4.setOccupation("Teacher");
        client4.setStatus("Matched");
        client4.setLastContact("2025-07-10");
        client4.setCompatibilityScore(78);
        clients.add(client4);


        return clients;
    }

    // Endpoint for Recent Matches: /api/matches/recent
    @GetMapping("/matches/recent")
    public List<MatchDTO> getRecentMatches() {
        List<MatchDTO> matches = new ArrayList<>();

        // Example ClientSubDTOs for matches (can reuse actual ClientDTOs if preferred)
        ClientSubDTO clientA = new ClientSubDTO(1L, "Ravi Kumar", "https://randomuser.me/api/portraits/men/1.jpg");
        ClientSubDTO clientB = new ClientSubDTO(2L, "Priya Sharma", "https://randomuser.me/api/portraits/women/2.jpg");
        ClientSubDTO clientC = new ClientSubDTO(3L, "Ahmed Khan", "https://randomuser.me/api/portraits/men/3.jpg");
        ClientSubDTO clientD = new ClientSubDTO(4L, "Sara Ali", "https://randomuser.me/api/portraits/women/4.jpg");

        MatchDTO match1 = new MatchDTO();
        match1.setId(101L);
        match1.setClient1(clientA);
        match1.setClient2(clientB);
        match1.setDate("2025-07-19");
        match1.setStatus("Finalized");
        match1.setCompatibility(88);
        match1.setCommission(15000.00);
        matches.add(match1);

        MatchDTO match2 = new MatchDTO();
        match2.setId(102L);
        match2.setClient1(clientC);
        match2.setClient2(clientD);
        match2.setDate("2025-07-17");
        match2.setStatus("Meeting Scheduled");
        match2.setCompatibility(75);
        match2.setCommission(0.00); // Commission not yet finalized
        matches.add(match2);

        return matches;
    }

    // Endpoint for Upcoming Tasks: /api/tasks/upcoming
    @GetMapping("/tasks/upcoming")
    public List<TaskDTO> getUpcomingTasks() {
        List<TaskDTO> tasks = new ArrayList<>();
        tasks.add(new TaskDTO(1L, "Collect biodata from new client: Arjun", "2025-07-22", "High"));
        tasks.add(new TaskDTO(2L, "Schedule meeting for Priya-Ravi match", "2025-07-24", "Medium"));
        tasks.add(new TaskDTO(3L, "Follow up with Sara's family for feedback", "2025-07-25", "High"));
        return tasks;
    }

    // Endpoint for Suggested Matches: /api/matches/suggested
    @GetMapping("/matches/suggested")
    public List<SuggestedMatchDTO> getSuggestedMatches() {
        List<SuggestedMatchDTO> suggested = new ArrayList<>();
        suggested.add(new SuggestedMatchDTO(1L, "Rohan Das", 30, "Financial Analyst", "https://randomuser.me/api/portraits/men/5.jpg", 89));
        suggested.add(new SuggestedMatchDTO(2L, "Anjali Singh", 29, "Marketing Manager", "https://randomuser.me/api/portraits/women/6.jpg", 84));
        return suggested;
    }

    // Endpoint for Commission Summary: /api/dashboard/commission-summary
    @GetMapping("/dashboard/commission-summary")
    public CommissionSummaryDTO getCommissionSummary() {
        CommissionSummaryDTO summary = new CommissionSummaryDTO();
        summary.setThisMonth(84500.00);
        summary.setTarget(130000.00);
        summary.setPendingPayments(23000.00);
        summary.setPendingMatches(4);
        return summary;
    }
}