package com.lamentaciones.supervision.domain.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SupervisionNotification {
    private String id;
    private String userId;
    private String type;     // BANNED | SUSPENDED | WARNING | BAN_LIFTED
    private String message;
    private boolean read;
    private Instant createdAt;
}