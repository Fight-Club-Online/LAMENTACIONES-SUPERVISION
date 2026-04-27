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

    // Verificar estado del usuario (llamado principalmente en el flujo de login)
    @GetMapping("/status/{userId}")
    public ResponseEntity<UserStatusResponse> checkStatus(@PathVariable String userId) {
        return ResponseEntity.ok(checkUserStatusUseCase.checkStatus(userId));
    }

    // Consulta del historial de chat de una pelea específica
    @GetMapping("/chat/{fightId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String fightId,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(getChatHistoryUseCase.getChatHistory(fightId, limit));
    }

    // Obtener notificaciones de acciones de supervisión (baneos, advertencias, etc.)
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<SupervisionNotification>> getNotifications(
            @PathVariable String userId) {
        return ResponseEntity.ok(getNotificationsUseCase.getUnreadNotifications(userId));
    }

    // Marcar las notificaciones del usuario como leídas
    @PatchMapping("/notifications/{userId}/read")
    public ResponseEntity<Void> markNotificationsRead(@PathVariable String userId) {
        getNotificationsUseCase.markAsRead(userId);
        return ResponseEntity.noContent().build();
    }
}