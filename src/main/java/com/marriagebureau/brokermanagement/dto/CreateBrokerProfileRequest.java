// src/main/java/com/marriagebureau/brokermanagement/dto/CreateBrokerProfileRequest.java
package com.marriagebureau.brokermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBrokerProfileRequest {

    @NotBlank(message = "Broker name is required")
    @Size(max = 100, message = "Broker name cannot exceed 100 characters")
    private String brokerName;

    @Size(max = 200, message = "Firm name cannot exceed 200 characters")
    // @NotBlank is not used here because firmName is optional
    private String firmName;

    @NotBlank(message = "Registration number is required")
    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits") // Assuming 6-digit Indian pincode
    private String pincode;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid firm contact number format (10-15 digits, optional +)")
    // @NotBlank is not used here because firmContactNumber is optional
    private String firmContactNumber;
}