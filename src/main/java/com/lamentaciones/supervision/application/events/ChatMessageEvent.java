package com.lamentaciones.supervision.application.events;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChatMessageEvent {
    private String fightId;
    private String userId;
    private String username;
    private String content;
    private boolean filtered;
    private boolean flagged;
    private Instant timestamp;
}