package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "NOTIFICATIONS")
public class NotificationDocument {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String type;
    private String message;
    private boolean read;
    private Instant createdAt;
}