package com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters;

import com.lamentaciones.supervision.domain.model.SupervisionNotification;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.NotificationDocument;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.NotificationMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoNotificationAdapter {

    private final NotificationMongoRepository mongoRepo;

    public MongoNotificationAdapter(NotificationMongoRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    public void save(SupervisionNotification notification) {
        NotificationDocument doc = toDocument(notification);
        mongoRepo.save(doc);
    }

    public List<SupervisionNotification> findUnreadByUserId(String userId) {
        return mongoRepo.findByUserIdAndReadFalse(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public void markAllAsRead(String userId) {
        List<NotificationDocument> unread = mongoRepo.findByUserIdAndReadFalse(userId);
        unread.forEach(doc -> doc.setRead(true));
        mongoRepo.saveAll(unread);
    }

    public List<SupervisionNotification> findAllByUserId(String userId) {
        return mongoRepo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDomain) // Reutiliza tu mapper toDomain
                .collect(Collectors.toList());
    }

    // MAPPERS
    private NotificationDocument toDocument(SupervisionNotification domain) {
        return NotificationDocument.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .type(domain.getType())
                .message(domain.getMessage())
                .read(domain.isRead())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    private SupervisionNotification toDomain(NotificationDocument doc) {
        return SupervisionNotification.builder()
                .id(doc.getId())
                .userId(doc.getUserId())
                .type(doc.getType())
                .message(doc.getMessage())
                .read(doc.isRead())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}