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
public class ReportService implements ReviewReportUseCase { 

    private final ReportRepository reportRepository;

    @Override
    public Report reviewReport(ReviewReportCommand command) {
        Report report = reportRepository.findById(command.getReportId())
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        ReportStatus currentStatus = (report.getReportStatus() == null)
                ? ReportStatus.PENDING
                : report.getReportStatus();

        ReportStatus nextStatus = command.getAction();

        if (nextStatus == ReportStatus.REVIEWED && currentStatus != ReportStatus.PENDING) {
            throw new IllegalStateException(
                    "Solo se puede revisar (REVIEWED) un reporte pendiente. Estado actual: " + currentStatus);
        }

        if ((nextStatus == ReportStatus.RESOLVED || nextStatus == ReportStatus.DISMISSED)
                && currentStatus != ReportStatus.REVIEWED) {
            throw new IllegalStateException(
                    "Para finalizar el caso, primero debe marcarlo como REVIEWED. Estado actual: " + currentStatus);
        }

        if (currentStatus == ReportStatus.RESOLVED || currentStatus == ReportStatus.DISMISSED) {
            throw new IllegalStateException("Este reporte ya está cerrado y no permite más cambios.");
        }

        report.setReportStatus(nextStatus);
        report.setAdminNotes(command.getNotes());
        report.setResolvedAt(Instant.now());

        log.info("[REPORT] Transición exitosa: {} -> {} (ID: {})", currentStatus, nextStatus, report.getId());
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

    @Override
    public List<Report> getAllReports(String status, int page, int size) {
        if (status != null && !status.isEmpty()) {
            return reportRepository.findByStatus(ReportStatus.valueOf(status.toUpperCase()), page, size);
        }
        return reportRepository.findAll(page, size);
    }
}