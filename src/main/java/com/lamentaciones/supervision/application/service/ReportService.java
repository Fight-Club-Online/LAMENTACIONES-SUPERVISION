package com.lamentaciones.supervision.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lamentaciones.supervision.application.commands.ReviewReportCommand;
import com.lamentaciones.supervision.application.ports.in.ReviewReportUseCase;
import com.lamentaciones.supervision.domain.enums.ReportStatus;
import com.lamentaciones.supervision.domain.model.Report;
import com.lamentaciones.supervision.domain.repository.ReportRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService implements ReviewReportUseCase {

    private final ReportRepository reportRepository;
    private final MeterRegistry meterRegistry;

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
        meterRegistry.counter("business.reports.action", "status", nextStatus.name()).increment();
        log.info("[REPORT] Transición exitosa: {} -> {} (ID: {})", currentStatus, nextStatus, report.getId());
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getPendingReports(int page, int size) {
        meterRegistry.counter("business.reports.interactions", "action", "fetch_pending").increment();
        return reportRepository.findByStatus(ReportStatus.PENDING, page, size);
    }

    @Override
    public List<Report> getReportsByUser(String userId) {
        meterRegistry.counter("business.reports.interactions", "action", "fetch_by_user").increment();
        return reportRepository.findByReportedUserId(userId);
    }

    @Override
    public List<Report> getAllReports(String status, int page, int size) {
        meterRegistry.counter("business.reports.interactions", "action", "fetch_all").increment();
        if (status != null && !status.isEmpty()) {
            meterRegistry.counter("business.reports.interactions", "action", "fetch_by_status").increment();
            return reportRepository.findByStatus(ReportStatus.valueOf(status.toUpperCase()), page, size);
        }
        meterRegistry.counter("business.reports.interactions", "action", "fetch_all").increment();
        return reportRepository.findAll(page, size);
    }
}