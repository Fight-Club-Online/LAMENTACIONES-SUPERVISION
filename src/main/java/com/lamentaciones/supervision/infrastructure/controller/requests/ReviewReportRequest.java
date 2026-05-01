package com.lamentaciones.supervision.infrastructure.controller.requests;


import com.lamentaciones.supervision.domain.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReportRequest {
    private ReportStatus action; 
    private String notes;
}
