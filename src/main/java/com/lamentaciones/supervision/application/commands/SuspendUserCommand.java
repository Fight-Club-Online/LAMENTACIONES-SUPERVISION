package com.lamentaciones.supervision.application.commands;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SuspendUserCommand {
    private String userId;
    private String username;
    private String adminId;
    private String reason;
    private String description;
    private Instant expiresAt;  
}