// src/main/java/com/marriagebureau/brokermanagement/dto/UpdateBrokerProfileRequest.java
package com.marriagebureau.brokermanagement.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Note: No @NotBlank annotations here, as all fields are optional for a partial update.
// If a field is present, its size/pattern constraints will still apply.
@Data
public class UpdateBrokerProfileRequest {

    @Size(max = 100, message = "Broker name cannot exceed 100 characters")
    private String brokerName;

    @Size(max = 200, message = "Firm name cannot exceed 200 characters")
    private String firmName;

    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits") // Assuming 6-digit Indian pincode
    private String pincode;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid firm contact number format (10-15 digits, optional +)")
    private String firmContactNumber;
}