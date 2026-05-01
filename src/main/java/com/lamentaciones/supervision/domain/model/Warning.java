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
    private String content;
    private Integer count;  
    private String source;    
    private Instant timestamp;
}