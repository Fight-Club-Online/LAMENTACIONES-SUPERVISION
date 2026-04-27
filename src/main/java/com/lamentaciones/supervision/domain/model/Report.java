package com.lamentaciones.supervision.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;
import com.lamentaciones.supervision.domain.enums.ReportStatus;
import com.lamentaciones.supervision.domain.enums.ReportType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "REPORT") // Crucial: Debe coincidir con el nombre en Atlas
public class Report {
    
    @Id
    private String id;

    @Field("emisorId") // Mapea 'reporterId' al campo 'emisorId' de Atlas
    private String reporterId;

    @Field("targetId") // Mapea 'reportedUserId' al campo 'targetId' de Atlas
    private String reportedUserId;

    @Field("motivo") // Mapea 'description' o 'type' al campo 'motivo' de Atlas
    private String description;

    @Field("fecha") // Mapea 'createdAt' al campo 'fecha' de Atlas
    private Instant createdAt;

    private String fightId; // Este coincide (aunque en la imagen es null)
    
    private String reporterUsername;
    private String reportedUsername;
    private ReportType type; 
    private List<String> evidenceMessages;
    private ReportStatus reportStatus;
    private String adminNotes;
    private Instant resolvedAt;
}