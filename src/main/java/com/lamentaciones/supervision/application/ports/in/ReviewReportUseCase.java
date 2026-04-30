package com.lamentaciones.supervision.application.ports.in;

import java.util.List;

import com.lamentaciones.supervision.application.commands.ReviewReportCommand;
import com.lamentaciones.supervision.domain.model.Report;

public interface ReviewReportUseCase {
    Report reviewReport(ReviewReportCommand command);
    List<Report> getPendingReports(int page, int size);
    List<Report> getReportsByUser(String userId);
    List<Report> getAllReports(String status, int page, int size);
}
