package com.lamentaciones.supervision.infrastructure.controller.requests;

import lombok.*;
import java.time.Instant;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
@Getter @Setter
public class SuspendUserRequest {
    private String adminId;
    private String reason;
    private String description;
    private Instant expiresAt;
}