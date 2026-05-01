package com.lamentaciones.supervision.domain.model;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String id;
    private String fightId;
    private String userId;
    private String username;
    private String content;
    private String source;
    private Integer count;
    private boolean filtered;             
    private boolean flagged;              
    private Instant timestamp;
}