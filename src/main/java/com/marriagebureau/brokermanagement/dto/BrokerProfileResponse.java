// src/main/java/com/marriagebureau/brokermanagement/dto/BrokerProfileResponse.java
package com.marriagebureau.brokermanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BrokerProfileResponse {
    private Long id; // The ID of the BrokerProfile itself
    private Long appUserId; // The ID of the associated AppUser (broker)

    private String brokerName;
    private String firmName;
    private String registrationNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String firmContactNumber;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}