package com.lamentaciones.supervision.application.ports.in;

import java.util.List;

import com.lamentaciones.supervision.domain.model.SupervisionNotification;

public interface GetUserNotificationsUseCase {
    List<SupervisionNotification> getUnreadNotifications(String userId);
    void markAsRead(String userId);
}