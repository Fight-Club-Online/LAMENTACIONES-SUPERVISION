package com.lamentaciones.supervision.application.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SaveChatMessageCommand {
    private String fightId;
    private String userId;
    private String username;
    private String content;
    private boolean filtered;
    private boolean flagged;
}
