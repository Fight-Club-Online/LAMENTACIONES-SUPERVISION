package com.lamentaciones.supervision.infrastructure.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "WARNINGS") // Apunta a la colección de censura
public class WarningDocument {
    @Id
    private String id;

    private String fightId; // Puede ser null según tu imagen
    private String userId;
    private String username;

    @Field("texto") // Mapea el mensaje censurado "****"
    private String content;

    @Field("timestamp")
    private Instant timestamp;

    private Integer count; // Campo único de esta colección
}