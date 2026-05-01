package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "WARNINGS") 
public class WarningDocument {
    @Id
    private String id;

    private String fightId;
    private String userId;
    private String username;

    @Field("texto") 
    private String content;

    private Integer count;

    private String source;

    @Field("timestamp")
    private Instant timestamp;

}