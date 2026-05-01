package com.lamentaciones.supervision.application.commands;


import com.lamentaciones.supervision.domain.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
@Getter @Setter
public class ReviewReportCommand {
    private String reportId;
    private ReportStatus action; 
    private String notes;
}