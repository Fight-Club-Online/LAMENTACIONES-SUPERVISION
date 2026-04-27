package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // Importante
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "MESSAGES") // Debe coincidir con Atlas
public class ChatMessageDocument {
    @Id
    private String id;

    @Indexed
    private String fightId; // En Atlas ya se llama fightId

    private String userId; // En Atlas ya se llama userId
    
    private String username; // En Atlas ya se llama username

    @Field("texto") // Mapea 'texto' de Atlas a 'content' en Java
    private String content;

    @Field("timestamp") // Mapea el campo de Atlas
    private Instant timestamp;

    private boolean filtered;
    private boolean flagged;
}