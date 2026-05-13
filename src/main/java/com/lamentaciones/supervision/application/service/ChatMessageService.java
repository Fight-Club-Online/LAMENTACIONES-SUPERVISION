package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.commands.SaveChatMessageCommand;
import com.lamentaciones.supervision.application.ports.in.GetChatHistoryUseCase;
import com.lamentaciones.supervision.domain.model.ChatMessage;
import com.lamentaciones.supervision.domain.repository.ChatMessageRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements GetChatHistoryUseCase {

    private final ChatMessageRepository chatMessageRepository;
    private final MeterRegistry meterRegistry;

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
        meterRegistry.counter("business.chat.messages.total").increment();

        if (command.isFiltered()) {
            meterRegistry.counter("business.chat.moderation", "action", "filtered").increment();
        }
        if (command.isFlagged()) {
            meterRegistry.counter("business.chat.moderation", "action", "flagged").increment();
        }
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getChatHistory(String fightId, int limit) {
        meterRegistry.counter("business.chat.queries", "type", "history").increment();
        return chatMessageRepository.findByFightIdOrderByTimestamp(fightId, limit);
    }

    @Override
    public List<ChatMessage> getFlaggedMessages(int page, int size) {
        meterRegistry.counter("business.chat.queries", "type", "flagged").increment();
        return chatMessageRepository.findFlagged(page, size);
    }
}