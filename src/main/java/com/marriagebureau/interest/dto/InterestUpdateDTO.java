package com.marriagebureau.interest.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterestUpdateDTO {
    @NotBlank private String status;
}