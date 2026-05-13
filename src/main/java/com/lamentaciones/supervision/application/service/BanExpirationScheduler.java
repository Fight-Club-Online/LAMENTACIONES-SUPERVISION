package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.UserBanDocument;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.UserBanMongoRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BanExpirationScheduler {

    private final UserBanMongoRepository banMongoRepository;
    private final NotificationService notificationService;
    private final MeterRegistry meterRegistry;
    @Scheduled(fixedRate = 60000) 
    public void checkAndCleanExpiredBans() {
        Instant now = Instant.now();

        List<UserBanDocument> expiredBans = banMongoRepository
                .findByStatusAndExpiresAtBefore("SUSPENDED", now);

        if (expiredBans.isEmpty()) {
            return;
        }

        log.info("[SCHEDULER] Se encontraron {} suspensiones expiradas para procesar.", expiredBans.size());

        for (UserBanDocument ban : expiredBans) {
            try {
                notificationService.sendNotification(
                    ban.getUserId(),
                    "BAN_LIFTED",
                    "Tu suspensión ha finalizado. Ya puedes acceder nuevamente a la plataforma."
                );

                banMongoRepository.delete(ban);
                meterRegistry.counter("business.bans.lifted.total").increment();
                log.info("[SCHEDULER] ÉXITO: Usuario {} notificado y ban eliminado físicamente.", ban.getUserId());
            } catch (Exception e) {
                log.error("[SCHEDULER] ERROR al procesar la expiración del usuario {}: {}", 
                    ban.getUserId(), e.getMessage());
            }
        }
    }
}