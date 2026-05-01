package com.lamentaciones.supervision.application.events;

import java.time.Instant;

import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserBannedEvent {
    private String userId;
    private SupervisionStatus status;
    private String reason;
    private Instant expiresAt;
}