package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.dto.UserStatusResponse;
import com.lamentaciones.supervision.application.ports.in.CheckUserStatusUseCase;
import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import com.lamentaciones.supervision.domain.model.UserBan;
import com.lamentaciones.supervision.domain.repository.UserBanRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckUserStatusService implements CheckUserStatusUseCase {

    private final UserBanRepository banRepository;
    private final MeterRegistry meterRegistry;

    @Override
    public UserStatusResponse checkStatus(String userId) {
        meterRegistry.counter("business.access.checks.total").increment();
        Optional<UserBan> activeBan = banRepository.findMostRecentByUserId(userId);
        if (activeBan.isEmpty()) {
            meterRegistry.counter("business.access.status", "result", "allowed").increment();
            return UserStatusResponse.builder()
                    .userId(userId)
                    .status(SupervisionStatus.ACTIVE)
                    .canAccess(true)
                    .message("Usuario activo")
                    .warningCount(0)
                    .build();
        }

        UserBan ban = activeBan.get();

        if (ban.getStatus() == SupervisionStatus.BANNED && ban.getExpiresAt() == null) {
            meterRegistry.counter("business.access.status", "result", "denied_permanent").increment();
            return UserStatusResponse.builder()
                    .userId(userId)
                    .status(SupervisionStatus.BANNED)
                    .canAccess(false)
                    .message("Tu cuenta ha sido baneada permanentemente por: " + ban.getReason())
                    .warningCount(ban.getWarningCount())
                    .build();
        }

        if (ban.getExpiresAt() != null && Instant.now().isBefore(ban.getExpiresAt())) {
            long remaining = ban.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
            meterRegistry.counter("business.access.status", "result", "denied_temporary").increment();
            return UserStatusResponse.builder()
                    .userId(userId)
                    .status(ban.getStatus())
                    .canAccess(false)
                    .message("Tu cuenta está suspendida. Razón: " + ban.getReason())
                    .expiresAt(ban.getExpiresAt())
                    .remainingSeconds(remaining)
                    .warningCount(ban.getWarningCount())
                    .build();
        }

        meterRegistry.counter("business.access.status", "result", "allowed_expired_ban").increment();
        return UserStatusResponse.builder()
                .userId(userId)
                .status(SupervisionStatus.ACTIVE)
                .canAccess(true)
                .message("Suspensión expirada")
                .warningCount(ban.getWarningCount())
                .build();
    }
}