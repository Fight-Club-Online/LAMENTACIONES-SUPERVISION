package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "BANS")
public class UserBanDocument {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String username;
    private String status;
    private String reason;
    private String description;
    private String adminId;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean notified;
    private int warningCount;
}