package com.lamentaciones.supervision.application.events;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SupervisionEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "supervision.events";

    public void publishUserBanned(UserBannedEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, "user.banned", event);
        } catch (Exception e) {
            log.warn("[RABBIT] No se pudo publicar UserBannedEvent: {}", e.getMessage());
        }
    }

    public void publishUserSuspended(UserSuspendedEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, "user.suspended", event);
        } catch (Exception e) {
            log.warn("[RABBIT] No se pudo publicar UserSuspendedEvent: {}", e.getMessage());
        }
    }

    public void publishBanLifted(BanLiftedEvent event) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, "ban.lifted", event);
        } catch (Exception e) {
            log.warn("[RABBIT] No se pudo publicar BanLiftedEvent: {}", e.getMessage());
        }
    }

    public void publishHighReportAlert(String userId, int count) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, "report.high.alert",
                Map.of("userId", userId, "count", count));
        } catch (Exception e) {
            log.warn("[RABBIT] No se pudo publicar HighReportAlert: {}", e.getMessage());
        }
    }
}