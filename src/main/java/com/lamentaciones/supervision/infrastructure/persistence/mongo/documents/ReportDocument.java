package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // Importante
import java.time.Instant;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "REPORT")
public class ReportDocument {
    @Id
    private String id;

    @Indexed
    @Field("targetId") // En tu imagen de Atlas es 'targetId'
    private String reportedUserId;

    @Field("emisorId") // En tu imagen de Atlas es 'emisorId'
    private String reporterId;

    @Field("motivo") // En tu imagen de Atlas es 'motivo'
    private String description;

    @Field("fecha") // En tu imagen de Atlas es 'fecha'
    private Instant createdAt;

    @Indexed
    private String fightId;

    private String reporterUsername;
    private String reportedUsername;
    private String type;
    private List<String> evidenceMessages;
    private String reportStatus;
    private String adminNotes;
    private Instant resolvedAt;
}