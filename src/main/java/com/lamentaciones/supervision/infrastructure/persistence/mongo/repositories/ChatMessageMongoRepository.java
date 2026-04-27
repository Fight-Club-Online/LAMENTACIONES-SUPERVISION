package com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.ChatMessageDocument;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {
    List<ChatMessageDocument> findByFightIdOrderByTimestampAsc(String fightId, Pageable pageable);
    List<ChatMessageDocument> findByFlaggedTrueOrderByTimestampDesc(Pageable pageable);
}