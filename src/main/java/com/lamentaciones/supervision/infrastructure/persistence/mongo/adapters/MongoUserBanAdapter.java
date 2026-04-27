package com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters;

import com.lamentaciones.supervision.domain.enums.SupervisionStatus;
import com.lamentaciones.supervision.domain.model.UserBan;
import com.lamentaciones.supervision.domain.repository.UserBanRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.UserBanMongoRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.UserBanDocument;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoUserBanAdapter implements UserBanRepository {

    private final UserBanMongoRepository mongoRepo;

    public MongoUserBanAdapter(UserBanMongoRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    @Override
    public UserBan save(UserBan userBan) {
        UserBanDocument doc = toDocument(userBan);
        UserBanDocument savedDoc = mongoRepo.save(doc);
        return toDomain(savedDoc);
    }

    @Override
    public Optional<UserBan> findActiveByUserId(String userId) {
        return mongoRepo.findByUserIdAndStatus(userId, "ACTIVE")
                .map(this::toDomain);
    }

    @Override
    public Optional<UserBan> findMostRecentByUserId(String userId) {
        return mongoRepo.findTopByUserIdOrderByCreatedAtDesc(userId)
                .map(this::toDomain);
    }

    @Override
    public List<UserBan> findHistoryByUserId(String userId) {
        return mongoRepo.findByUserId(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserId(String userId) {
        // Borrado directo y atómico en MongoDB usando el campo userId
        if (userId != null) {
            mongoRepo.deleteByUserId(userId);
        }
    }

    // =============================================================
    // LÓGICA DE MAPEO (MAPPERS)
    // =============================================================

    private UserBanDocument toDocument(UserBan domain) {
        if (domain == null)
            return null;
        return UserBanDocument.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .reason(domain.getReason())
                .adminId(domain.getAdminId())
                .createdAt(domain.getCreatedAt())
                .expiresAt(domain.getExpiresAt())
                .notified(domain.isNotified())
                .warningCount(domain.getWarningCount())
                .build();
    }

    private UserBan toDomain(UserBanDocument doc) {
        if (doc == null)
            return null;
        return UserBan.builder()
                .id(doc.getId())
                .userId(doc.getUserId())
                .username(doc.getUsername())
                // Usa el Enum correcto: SupervisionStatus
                .status(doc.getStatus() != null ? SupervisionStatus.valueOf(doc.getStatus().toUpperCase()) : null)
                .reason(doc.getReason())
                .adminId(doc.getAdminId())
                .createdAt(doc.getCreatedAt())
                .expiresAt(doc.getExpiresAt())
                .warningCount(doc.getWarningCount())
                .build();
    }
}