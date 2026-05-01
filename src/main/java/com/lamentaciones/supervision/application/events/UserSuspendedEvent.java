package com.lamentaciones.supervision.application.events;


import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserSuspendedEvent {
    private String userId;
    private SupervisionStatus status;
    private String reason;
    private Instant expiresAt;
}