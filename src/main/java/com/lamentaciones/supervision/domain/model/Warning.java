package com.lamentaciones.supervision.domain.model;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Warning {
    private String id;
    private String fightId;
    private String userId;
    private String username;
    private String content;   // El texto censurado "****"
    private Integer count;    // ¡Importante! Para ver la reincidencia
    private Instant timestamp;
}