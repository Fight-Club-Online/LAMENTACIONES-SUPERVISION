package com.lamentaciones.supervision.application.ports.in;

import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import java.util.List;

public interface GetUserNotificationsUseCase {
    List<SupervisionNotification> getUnreadNotifications(String userId);
    List<SupervisionNotification> getAllNotifications(String userId); 
    void markAsRead(String userId);
}