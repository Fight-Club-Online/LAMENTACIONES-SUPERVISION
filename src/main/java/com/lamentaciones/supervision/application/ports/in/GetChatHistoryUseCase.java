package com.lamentaciones.supervision.application.ports.in;

import java.util.List;

import com.lamentaciones.supervision.domain.model.ChatMessage;

public interface GetChatHistoryUseCase {
    List<ChatMessage> getChatHistory(String fightId, int limit);
    List<ChatMessage> getFlaggedMessages(int page, int size);
}
