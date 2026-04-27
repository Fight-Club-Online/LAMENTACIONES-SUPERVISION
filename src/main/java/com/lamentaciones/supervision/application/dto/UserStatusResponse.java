package com.lamentaciones.supervision.application.dto;

import java.time.Instant;

import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserStatusResponse {
    private String userId;
    private SupervisionStatus status;
    private boolean canAccess;            // false si BANNED o SUSPENDED vigente
    private String message;              // mensaje para mostrar al usuario
    private Instant expiresAt;           // cuándo termina la suspensión
    private long remainingSeconds;       // segundos restantes
    private int warningCount;
    private boolean hasUnreadNotifications;
}