package com.marriagebureau.interest.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterestRequestDTO {
    @NotNull private Long senderProfileId;
    @NotNull private Long receiverProfileId;
}