package com.lamentaciones.supervision.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.lamentaciones.supervision.application.commands.SaveChatMessageCommand;
import com.lamentaciones.supervision.application.events.ChatMessageEvent;
import com.lamentaciones.supervision.application.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageEventListener {

    private final ChatMessageService chatMessageService;

    @RabbitListener(queues = "supervision.chat.messages")
    public void handleChatMessage(ChatMessageEvent event) {
        chatMessageService.saveMessage(SaveChatMessageCommand.builder()
            .fightId(event.getFightId())
            .userId(event.getUserId())
            .username(event.getUsername())
            .content(event.getContent())
            .filtered(event.isFiltered())
            .flagged(event.isFlagged())
            .build());
    }
}