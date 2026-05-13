package com.lamentaciones.supervision.application.service;

import com.lamentaciones.supervision.application.ports.in.GetUserNotificationsUseCase;
import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters.MongoNotificationAdapter;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationService implements GetUserNotificationsUseCase {

    private final MongoNotificationAdapter notificationAdapter;
    private final MeterRegistry meterRegistry;

    public void sendNotification(String userId, String type, String message) {
        SupervisionNotification notification = SupervisionNotification.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .read(false)
                .createdAt(Instant.now())
                .build();
        notificationAdapter.save(notification);
        meterRegistry.counter("business.notifications.sent", "type", type).increment();

    }

    @Override
    public List<SupervisionNotification> getUnreadNotifications(String userId) {
        meterRegistry.counter("business.notifications.interactions", "action", "fetch_unread").increment();
        return notificationAdapter.findUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(String userId) {
        notificationAdapter.markAllAsRead(userId);
        meterRegistry.counter("business.notifications.interactions", "action", "read").increment();
    }

    @Override
    public List<SupervisionNotification> getAllNotifications(String userId) {
        meterRegistry.counter("business.notifications.interactions", "action", "viewed").increment();
        return notificationAdapter.findAllByUserId(userId);
    }

}