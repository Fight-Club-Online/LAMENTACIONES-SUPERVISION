package com.lamentaciones.supervision.application.events;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupervisionEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "supervision.events";

    public void publishUserBanned(UserBannedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "user.banned", event);
    }

    public void publishUserSuspended(UserSuspendedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "user.suspended", event);
    }

    public void publishBanLifted(BanLiftedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "ban.lifted", event);
    }

    public void publishHighReportAlert(String userId, int count) {
        rabbitTemplate.convertAndSend(EXCHANGE, "report.high.alert",
            Map.of("userId", userId, "count", count));
    }
}