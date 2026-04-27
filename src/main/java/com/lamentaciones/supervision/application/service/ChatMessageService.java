package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.commands.SaveChatMessageCommand;
import com.lamentaciones.supervision.application.ports.in.GetChatHistoryUseCase;
import com.lamentaciones.supervision.domain.model.ChatMessage;
import com.lamentaciones.supervision.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements GetChatHistoryUseCase {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(SaveChatMessageCommand command) {
        ChatMessage message = ChatMessage.builder()
            .fightId(command.getFightId())
            .userId(command.getUserId())
            .username(command.getUsername())
            .content(command.getContent())
            .filtered(command.isFiltered())
            .flagged(command.isFlagged())
            .timestamp(Instant.now())
            .build();
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getChatHistory(String fightId, int limit) {
        return chatMessageRepository.findByFightIdOrderByTimestamp(fightId, limit);
    }

    @Override
    public List<ChatMessage> getFlaggedMessages(int page, int size) {
        return chatMessageRepository.findFlagged(page, size);
    }
}