package com.lamentaciones.supervision.infrastructure.controller.requests;

import lombok.*;
import java.time.Instant;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
@Getter @Setter // Añade estos dos para forzar la generación
public class SuspendUserRequest {
    private String username;
    private String adminId;
    private String reason;
    private Instant expiresAt;
}