package com.lamentaciones.supervision.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;
import com.lamentaciones.supervision.domain.enums.ReportStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "REPORT") 
public class Report {
    
    @Id
    private String id;

    @Field("emisorId") 
    private String reporterId;

    @Field("targetId") 
    private String reportedUserId;

    @Field("motivo") 
    private String description;

    @Field("fecha") 
    private Instant createdAt;
    private String fightId; 
    private String reporterUsername;
    private String reportedUsername;
    private List<String> evidenceMessages;
    private ReportStatus reportStatus;
    private String adminNotes;
    private Instant resolvedAt;
}