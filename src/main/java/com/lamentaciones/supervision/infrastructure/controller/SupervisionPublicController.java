package com.lamentaciones.supervision.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lamentaciones.supervision.application.dto.UserStatusResponse;
import com.lamentaciones.supervision.application.ports.in.CheckUserStatusUseCase;
import com.lamentaciones.supervision.application.ports.in.GetChatHistoryUseCase;
import com.lamentaciones.supervision.application.ports.in.GetUserNotificationsUseCase;
import com.lamentaciones.supervision.domain.model.ChatMessage;
import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/supervision")
@RequiredArgsConstructor
public class SupervisionPublicController {

    private final CheckUserStatusUseCase checkUserStatusUseCase;
    private final GetChatHistoryUseCase getChatHistoryUseCase;
    private final GetUserNotificationsUseCase getNotificationsUseCase;
    @GetMapping("/status/{userId}")
    public ResponseEntity<UserStatusResponse> checkStatus(@PathVariable String userId) {
        return ResponseEntity.ok(checkUserStatusUseCase.checkStatus(userId));
    }
    @GetMapping("/chat/{fightId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String fightId,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(getChatHistoryUseCase.getChatHistory(fightId, limit));
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<SupervisionNotification>> getNotifications(
            @PathVariable String userId) {
        return ResponseEntity.ok(getNotificationsUseCase.getUnreadNotifications(userId));
    }

    @PatchMapping("/notifications/{userId}/read")
    public ResponseEntity<Void> markNotificationsRead(@PathVariable String userId) {
        getNotificationsUseCase.markAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notifications/{userId}/unread")
    public ResponseEntity<List<SupervisionNotification>> getUnreadNotifications(
            @PathVariable String userId) {
        return ResponseEntity.ok(getNotificationsUseCase.getUnreadNotifications(userId));
    }

    @GetMapping("/notifications/{userId}/history")
    public ResponseEntity<List<SupervisionNotification>> getNotificationHistory(
            @PathVariable String userId) {
        return ResponseEntity.ok(getNotificationsUseCase.getAllNotifications(userId));
    }

}