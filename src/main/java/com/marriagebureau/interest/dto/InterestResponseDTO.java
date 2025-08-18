package com.marriagebureau.interest.dto;
import com.marriagebureau.clientmanagement.dto.ProfileResponse;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
public class InterestResponseDTO {
    private Long id;
    private ProfileResponse senderProfile;
    private ProfileResponse receiverProfile;
    private String status;
    private LocalDateTime sentAt;
}