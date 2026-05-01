package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "MESSAGES")
public class ChatMessageDocument {
    @Id
    private String id;

    @Indexed
    private String fightId; 

    private String userId; 
    
    private String username; 

    @Field("texto") 
    private String content;

    private String source;

    private Integer count;

    @Field("timestamp") 
    private Instant timestamp;

    private boolean filtered;
    private boolean flagged;
}