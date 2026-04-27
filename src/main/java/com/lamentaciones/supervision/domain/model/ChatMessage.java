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
    private boolean filtered;             // fue modificado por filtro de palabras
    private boolean flagged;              // fue marcado para revisión
    private Instant timestamp;
}