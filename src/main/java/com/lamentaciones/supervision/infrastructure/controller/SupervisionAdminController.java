package com.lamentaciones.supervision.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lamentaciones.supervision.application.commands.BanUserCommand;
import com.lamentaciones.supervision.application.commands.ReviewReportCommand;
import com.lamentaciones.supervision.application.commands.SuspendUserCommand;
import com.lamentaciones.supervision.application.dto.UserStatusResponse;
import com.lamentaciones.supervision.application.ports.in.BanUserUseCase;
import com.lamentaciones.supervision.application.ports.in.CheckUserStatusUseCase;
import com.lamentaciones.supervision.application.ports.in.GetChatHistoryUseCase;
import com.lamentaciones.supervision.application.ports.in.LiftBanUseCase;
import com.lamentaciones.supervision.application.ports.in.ReviewReportUseCase;
import com.lamentaciones.supervision.application.ports.in.SuspendUserUseCase;
import com.lamentaciones.supervision.domain.enums.BanReason;
import com.lamentaciones.supervision.domain.model.ChatMessage;
import com.lamentaciones.supervision.domain.model.Report;
import com.lamentaciones.supervision.domain.model.UserBan;
import com.lamentaciones.supervision.infrastructure.controller.requests.BanUserRequest;
import com.lamentaciones.supervision.infrastructure.controller.requests.ReviewReportRequest;
import com.lamentaciones.supervision.infrastructure.controller.requests.SuspendUserRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/supervision")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Solo admins
public class SupervisionAdminController {

    private final BanUserUseCase banUserUseCase;
    private final SuspendUserUseCase suspendUserUseCase;
    private final LiftBanUseCase liftBanUseCase;
    private final ReviewReportUseCase reviewReportUseCase;
    private final CheckUserStatusUseCase checkUserStatusUseCase;
    private final GetChatHistoryUseCase getChatHistoryUseCase;

    // ── BAN / SUSPEND ─────────────────────────────────────────────

    @PostMapping("/ban/{userId}")
    public ResponseEntity<UserBan> banUser(
            @PathVariable String userId,
            @RequestBody BanUserRequest request) {
        return ResponseEntity.ok(banUserUseCase.banUser(
                BanUserCommand.builder()
                        .userId(userId)
                        .username(request.getUsername())
                        .adminId(request.getAdminId())
                        .reason(BanReason.valueOf(request.getReason().toUpperCase()))
                        .description(request.getDescription())
                        .expiresAt(request.getExpiresAt()) // null = permanente
                        .build()));
    }

    @PostMapping("/suspend/{userId}")
    public ResponseEntity<UserBan> suspendUser(
            @PathVariable String userId,
            @RequestBody SuspendUserRequest request) {
        return ResponseEntity.ok(suspendUserUseCase.suspendUser(
                SuspendUserCommand.builder()
                        .userId(userId)
                        .username(request.getUsername())
                        .adminId(request.getAdminId())
                        .reason(request.getReason())
                        .expiresAt(request.getExpiresAt())
                        .build()));
    }

    @DeleteMapping("/ban/{userId}")
    public ResponseEntity<Void> liftBan(
            @PathVariable String userId,
            @RequestParam String adminId) {
        liftBanUseCase.liftBan(userId, adminId);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content (Éxito)
    }
    // ── REPORTS ───────────────────────────────────────────────────

    @GetMapping("/reports/pending")
    public ResponseEntity<List<Report>> getPendingReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewReportUseCase.getPendingReports(page, size));
    }

    @GetMapping("/reports/user/{userId}")
    public ResponseEntity<List<Report>> getReportsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reviewReportUseCase.getReportsByUser(userId));
    }

    @PatchMapping("/reports/{reportId}")
    public ResponseEntity<Report> reviewReport(
            @PathVariable String reportId,
            @RequestBody ReviewReportRequest request) {
        return ResponseEntity.ok(reviewReportUseCase.reviewReport(
                ReviewReportCommand.builder()
                        .reportId(reportId)
                        .action(request.getAction())
                        .notes(request.getNotes())
                        .build()));
    }

    // ── STATUS ────────────────────────────────────────────────────

    @GetMapping("/status/{userId}")
    public ResponseEntity<UserStatusResponse> getUserStatus(@PathVariable String userId) {
        return ResponseEntity.ok(checkUserStatusUseCase.checkStatus(userId));
    }

    // ── CHAT HISTORY ──────────────────────────────────────────────

    @GetMapping("/chat/{fightId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String fightId,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(getChatHistoryUseCase.getChatHistory(fightId, limit));
    }

    @GetMapping("/chat/flagged")
    public ResponseEntity<List<ChatMessage>> getFlaggedMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(getChatHistoryUseCase.getFlaggedMessages(page, size));
    }
}
