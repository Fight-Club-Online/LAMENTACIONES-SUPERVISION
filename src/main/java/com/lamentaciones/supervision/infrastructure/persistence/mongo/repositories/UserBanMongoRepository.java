package com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.UserBanDocument;

public interface UserBanMongoRepository extends MongoRepository<UserBanDocument, String> {
    Optional<UserBanDocument> findTopByUserIdOrderByCreatedAtDesc(String userId);
    List<UserBanDocument> findByUserId(String userId);
    List<UserBanDocument> findByStatus(String status);
    Optional<UserBanDocument> findByUserIdAndStatus(String userId, String status);
    @Transactional
    void deleteByUserId(String userId);
}