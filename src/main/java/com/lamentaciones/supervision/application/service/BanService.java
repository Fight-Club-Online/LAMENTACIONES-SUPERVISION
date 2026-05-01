package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.lamentaciones.supervision.domain.model.Report;
import com.lamentaciones.supervision.domain.repository.UserBanRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.WarningMongoRepository;
import com.lamentaciones.supervision.domain.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BanService implements BanUserUseCase, SuspendUserUseCase, LiftBanUseCase {

        private final UserBanRepository banRepository;
        private final ReportRepository reportRepository;
        private final WarningMongoRepository warningRepo;
        private final SupervisionEventPublisher eventPublisher;
        private final NotificationService notificationService;
        private static final int MIN_REPORTS_REQUIRED = 3;

        @Override
        @Transactional
        public UserBan banUser(BanUserCommand command) {
                validateReportCount(command.getUserId());
                int totalWarnings = (int) warningRepo.countByUserId(command.getUserId());
                banRepository.findActiveByUserId(command.getUserId()).ifPresent(existing -> {
                        if (existing.getStatus() == SupervisionStatus.BANNED) {
                                log.warn("[BAN] El usuario {} ya tiene un baneo permanente.", command.getUserId());
                                throw new IllegalStateException("El usuario ya se encuentra baneado permanentemente.");
                        }
                        log.info("[BAN] Escalando sanción: Usuario {} pasará de SUSPENDED a BANNED",
                                        command.getUserId());
                });
                banRepository.deleteByUserId(command.getUserId());

                String automaticUsername = fetchUsernameFromReports(command.getUserId());

                UserBan ban = UserBan.builder()
                                .userId(command.getUserId())
                                .username(automaticUsername)
                                .status(SupervisionStatus.BANNED)
                                .reason(command.getReason() + ": " + command.getDescription())
                                .adminId(command.getAdminId())
                                .createdAt(Instant.now())
                                .expiresAt(null)
                                .notified(true)
                                .warningCount(totalWarnings)
                                .build();

                UserBan saved = banRepository.save(ban);

                notificationService.sendNotification(
                                command.getUserId(),
                                "BANNED",
                                "Tu cuenta ha sido bloqueada permanentemente. Motivo: " + command.getReason());

                eventPublisher.publishUserBanned(UserBannedEvent.builder()
                                .userId(command.getUserId())
                                .status(SupervisionStatus.BANNED)
                                .reason(saved.getReason())
                                .expiresAt(null)
                                .build());

                return saved;
        }

        @Override
        @Transactional
        public UserBan suspendUser(SuspendUserCommand command) {
                if (command.getExpiresAt() == null) {
                        throw new IllegalArgumentException(
                                        "La suspensión requiere una fecha de expiración obligatoria.");
                }

                int totalWarnings = (int) warningRepo.countByUserId(command.getUserId());

                validateReportCount(command.getUserId());

                banRepository.findActiveByUserId(command.getUserId()).ifPresent(existing -> {
                        if (existing.getStatus() == SupervisionStatus.BANNED) {
                                throw new IllegalStateException(
                                                "No se puede suspender a un usuario que ya tiene un baneo permanente.");
                        }
                        log.info("[SUSPEND] Reemplazando sanción previa para el usuario {}", command.getUserId());
                        banRepository.deleteByUserId(command.getUserId());
                });

                String automaticUsername = fetchUsernameFromReports(command.getUserId());

                UserBan suspension = UserBan.builder()
                                .userId(command.getUserId())
                                .username(automaticUsername)
                                .status(SupervisionStatus.SUSPENDED)
                                .reason(command.getReason() + ": " + command.getDescription())
                                .adminId(command.getAdminId())
                                .createdAt(Instant.now())
                                .expiresAt(command.getExpiresAt())
                                .notified(true)
                                .warningCount(totalWarnings)
                                .build();

                UserBan saved = banRepository.save(suspension);

                notificationService.sendNotification(
                                command.getUserId(),
                                "SUSPENDED",
                                "Tu cuenta ha sido suspendida temporalmente hasta el " + command.getExpiresAt());

                eventPublisher.publishUserSuspended(UserSuspendedEvent.builder()
                                .userId(command.getUserId())
                                .status(SupervisionStatus.SUSPENDED)
                                .reason(saved.getReason())
                                .expiresAt(saved.getExpiresAt())
                                .build());

                return saved;
        }

        @Override
        @Transactional
        public UserBan liftBan(String userId, String adminId) {
                UserBan ban = banRepository.findActiveByUserId(userId)
                                .orElseThrow(() -> new RuntimeException(
                                                "No existe una sanción activa para este usuario: " + userId));

                banRepository.deleteByUserId(userId);

                notificationService.sendNotification(userId, "BAN_LIFTED", "Tu cuenta ha sido habilitada nuevamente.");

                eventPublisher.publishBanLifted(BanLiftedEvent.builder()
                                .userId(userId)
                                .adminId(adminId)
                                .build());

                return ban;
        }

        /**
         * Verifica que el usuario cuente con el volumen de reportes necesario para
         * proceder.
         */
        private void validateReportCount(String userId) {
                List<Report> reports = reportRepository.findByReportedUserId(userId);
                int currentCount = reports.size();

                if (currentCount < MIN_REPORTS_REQUIRED) {
                        log.warn("[VALIDATION] Intento de sanción fallido: Usuario {} tiene {} reportes, se requieren {}",
                                        userId, currentCount, MIN_REPORTS_REQUIRED);
                        throw new IllegalStateException("Acción denegada: El usuario debe tener al menos "
                                        + MIN_REPORTS_REQUIRED + " reportes para ser sancionado (Actual: "
                                        + currentCount + ")");
                }
        }

        private String fetchUsernameFromReports(String userId) {
                return reportRepository.findByReportedUserId(userId)
                                .stream()
                                .findFirst()
                                .map(Report::getReportedUsername)
                                .orElse("Usuario_Desconocido");
        }
}