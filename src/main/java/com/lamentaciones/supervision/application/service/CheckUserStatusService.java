package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.dto.UserStatusResponse;
import com.lamentaciones.supervision.application.ports.in.CheckUserStatusUseCase;
import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import com.lamentaciones.supervision.domain.model.UserBan;
import com.lamentaciones.supervision.domain.repository.UserBanRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckUserStatusService implements CheckUserStatusUseCase {

    private final UserBanRepository banRepository;

    @Override
    public UserStatusResponse checkStatus(String userId) {
        Optional<UserBan> activeBan = banRepository.findMostRecentByUserId(userId);

        if (activeBan.isEmpty()) {
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

        return UserStatusResponse.builder()
            .userId(userId)
            .status(SupervisionStatus.ACTIVE)
            .canAccess(true)
            .message("Suspensión expirada")
            .warningCount(ban.getWarningCount())
            .build();
    }
}