package com.lamentaciones.supervision.infrastructure.controller.requests;

import lombok.Data;
import java.time.Instant;

@Data
public class BanUserRequest {
    private String adminId;
    private String reason;
    private String description;
    private Instant expiresAt;
}