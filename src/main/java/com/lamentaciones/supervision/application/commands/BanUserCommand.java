package com.lamentaciones.supervision.application.commands;

import java.time.Instant;

import com.lamentaciones.supervision.domain.enums.BanReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BanUserCommand {
    private String userId;
    private String username;
    private String adminId;
    private BanReason reason;
    private String description;
    private Instant expiresAt;   
}