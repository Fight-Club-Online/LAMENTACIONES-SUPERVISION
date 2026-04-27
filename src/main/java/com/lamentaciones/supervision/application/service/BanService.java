package com.lamentaciones.supervision.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.commands.BanUserCommand;
import com.lamentaciones.supervision.application.commands.SuspendUserCommand;
import com.lamentaciones.supervision.application.events.BanLiftedEvent;
import com.lamentaciones.supervision.application.events.SupervisionEventPublisher;
import com.lamentaciones.supervision.application.events.UserBannedEvent;
import com.lamentaciones.supervision.application.events.UserSuspendedEvent;
import com.lamentaciones.supervision.application.ports.in.BanUserUseCase;
import com.lamentaciones.supervision.application.ports.in.LiftBanUseCase;
import com.lamentaciones.supervision.application.ports.in.SuspendUserUseCase;
import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import com.lamentaciones.supervision.domain.model.UserBan;
import com.lamentaciones.supervision.domain.repository.UserBanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BanService implements BanUserUseCase, SuspendUserUseCase, LiftBanUseCase {

    private final UserBanRepository banRepository;
    private final SupervisionEventPublisher eventPublisher;

    @Override
    public UserBan banUser(BanUserCommand command) {
        // Verificar si ya tiene ban activo
        banRepository.findActiveByUserId(command.getUserId())
                .ifPresent(existing -> {
                    existing.setStatus(SupervisionStatus.BANNED);
                    existing.setExpiresAt(null); // permanente
                    banRepository.save(existing);
                });

        UserBan ban = UserBan.builder()
                .userId(command.getUserId())
                .username(command.getUsername())
                .status(SupervisionStatus.BANNED)
                .reason(command.getReason() + ": " + command.getDescription())
                .adminId(command.getAdminId())
                .createdAt(Instant.now())
                .expiresAt(command.getExpiresAt()) // null = permanente
                .notified(false)
                .build();

        UserBan saved = banRepository.save(ban);

        // Publicar evento para que el módulo de auth bloquee el acceso
        eventPublisher.publishUserBanned(UserBannedEvent.builder()
                .userId(command.getUserId())
                .status(SupervisionStatus.BANNED)
                .reason(ban.getReason())
                .expiresAt(command.getExpiresAt())
                .build());

        log.info("[BAN] Usuario {} baneado por admin {}", command.getUserId(), command.getAdminId());
        return saved;
    }

    @Override
    public UserBan suspendUser(SuspendUserCommand command) {
        if (command.getExpiresAt() == null) {
            throw new IllegalArgumentException("Suspensión requiere fecha de expiración");
        }

        UserBan suspension = UserBan.builder()
                .userId(command.getUserId())
                .username(command.getUsername())
                .status(SupervisionStatus.SUSPENDED)
                .reason(command.getReason())
                .adminId(command.getAdminId())
                .createdAt(Instant.now())
                .expiresAt(command.getExpiresAt())
                .notified(false)
                .build();

        UserBan saved = banRepository.save(suspension);

        eventPublisher.publishUserSuspended(UserSuspendedEvent.builder()
                .userId(command.getUserId())
                .status(SupervisionStatus.SUSPENDED)
                .reason(command.getReason())
                .expiresAt(command.getExpiresAt())
                .build());

        return saved;
    }

    @Override
    public UserBan liftBan(String userId, String adminId) {
        // 1. Buscamos si existe para poder publicar el evento antes de borrar
        UserBan ban = banRepository.findMostRecentByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No existe el usuario en la tabla de baneos"));

        // 2. Borramos físicamente de MongoDB
        banRepository.deleteByUserId(userId);

        // 3. Notificamos
        eventPublisher.publishBanLifted(BanLiftedEvent.builder()
                .userId(userId)
                .adminId(adminId)
                .build());

        return ban;
    }
}