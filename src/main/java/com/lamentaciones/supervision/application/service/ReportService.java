package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.commands.ReviewReportCommand;
import com.lamentaciones.supervision.application.ports.in.ReviewReportUseCase;
import com.lamentaciones.supervision.domain.enums.ReportStatus;
import com.lamentaciones.supervision.domain.model.Report;
import com.lamentaciones.supervision.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService implements ReviewReportUseCase { // Eliminamos SubmitReportUseCase

    private final ReportRepository reportRepository;

    @Override
    public Report reviewReport(ReviewReportCommand command) {
        Report report = reportRepository.findById(command.getReportId())
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        report.setReportStatus(command.getAction()); // RESOLVED, DISMISSED
        report.setAdminNotes(command.getNotes());
        report.setResolvedAt(Instant.now());

        log.info("[REPORT] Reporte {} revisado con acción: {}", command.getReportId(), command.getAction());
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getPendingReports(int page, int size) {
        return reportRepository.findByStatus(ReportStatus.PENDING, page, size);
    }

    @Override
    public List<Report> getReportsByUser(String userId) {
        return reportRepository.findByReportedUserId(userId);
    }
}