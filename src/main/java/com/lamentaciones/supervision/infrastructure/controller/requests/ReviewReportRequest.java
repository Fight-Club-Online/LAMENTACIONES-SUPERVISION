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
    // El reportId usualmente viene en el @PathVariable, 
    // pero incluimos action y notes para el cuerpo del JSON.
    private ReportStatus action; // RESOLVED o DISMISSED
    private String notes;
}
