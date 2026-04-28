package com.lamentaciones.supervision.application.service;

import com.lamentaciones.supervision.application.ports.in.GetUserNotificationsUseCase;
import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters.MongoNotificationAdapter;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationService implements GetUserNotificationsUseCase {

    private final MongoNotificationAdapter notificationAdapter;

    // Método para ser usado internamente por BanService
    public void sendNotification(String userId, String type, String message) {
        SupervisionNotification notification = SupervisionNotification.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .read(false)
                .createdAt(Instant.now())
                .build();
        notificationAdapter.save(notification);
    }

    @Override
    public List<SupervisionNotification> getUnreadNotifications(String userId) {
        return notificationAdapter.findUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(String userId) {
        notificationAdapter.markAllAsRead(userId);
    }

    @Override
    public List<SupervisionNotification> getAllNotifications(String userId) {
        // Llama al adaptador para traer todo el historial ordenado
        return notificationAdapter.findAllByUserId(userId);
    }
}