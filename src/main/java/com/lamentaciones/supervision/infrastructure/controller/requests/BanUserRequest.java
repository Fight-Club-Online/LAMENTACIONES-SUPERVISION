package com.lamentaciones.supervision.infrastructure.controller.requests;

import lombok.Data;

@Data
public class BanUserRequest {
    private String adminId;
    private String reason;
    private String description;
}