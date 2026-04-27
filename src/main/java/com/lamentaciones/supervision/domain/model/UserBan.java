package com.lamentaciones.supervision.domain.model;

import lombok.*;
import java.time.Instant;

import com.lamentaciones.supervision.domain.enums.SupervisionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBan {
    private String id;
    private String userId;
    private String username;
    private SupervisionStatus status;     // BANNED | SUSPENDED | ACTIVE | WARNING
    private String reason;
    private String adminId;
    private Instant createdAt;
    private Instant expiresAt;            // null = permanente (BANNED)
    private boolean notified;
    private int warningCount;
}