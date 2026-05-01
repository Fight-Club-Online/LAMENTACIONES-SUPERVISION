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
    private boolean canAccess;            
    private String message;              
    private Instant expiresAt;           
    private long remainingSeconds;       
    private int warningCount;
    private boolean hasUnreadNotifications;
}