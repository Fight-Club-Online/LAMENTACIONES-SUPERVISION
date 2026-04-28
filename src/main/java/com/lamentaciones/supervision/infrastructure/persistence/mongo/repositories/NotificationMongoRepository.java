package com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {
    List<NotificationDocument> findByUserIdOrderByCreatedAtDesc(String userId);
    List<NotificationDocument> findByUserIdAndReadFalse(String userId);
}