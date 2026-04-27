package com.lamentaciones.supervision.application.service;

import com.lamentaciones.supervision.application.ports.in.GetUserNotificationsUseCase;
import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service // <--- ESTO ES LO QUE BUSCA EL CONTROLLER
public class NotificationService implements GetUserNotificationsUseCase {

    @Override
    public List<SupervisionNotification> getUnreadNotifications(String userId) {
        // Por ahora retornamos una lista vacía para que no falle
        return new ArrayList<>();
    }

    @Override
    public void markAsRead(String userId) {
        // Lógica para marcar como leído en el futuro
    }
}