package com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters;

import com.lamentaciones.supervision.domain.model.ChatMessage;
import com.lamentaciones.supervision.domain.repository.ChatMessageRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.ChatMessageMongoRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.WarningMongoRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.ChatMessageDocument;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.WarningDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoChatMessageAdapter implements ChatMessageRepository {

    private final ChatMessageMongoRepository mongoRepo;
    private final WarningMongoRepository warningRepo;

    public MongoChatMessageAdapter(ChatMessageMongoRepository mongoRepo, WarningMongoRepository warningRepo) {
        this.mongoRepo = mongoRepo;
        this.warningRepo = warningRepo;
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        ChatMessageDocument doc = toDocument(message);
        return toDomain(mongoRepo.save(doc));
    }

    @Override
    public List<ChatMessage> findByFightIdOrderByTimestamp(String fightId, int limit) {
        return mongoRepo.findByFightIdOrderByTimestampAsc(fightId, PageRequest.of(0, limit))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessage> findFlagged(int skip, int limit) {
        int page = limit > 0 ? skip / limit : 0;
        PageRequest pageable = PageRequest.of(page, limit);
        
        return warningRepo.findAllByOrderByTimestampDesc(pageable) 
                .stream()
                .map(this::warningToDomain)
                .collect(Collectors.toList());
    }

    // =============================================================
    // MAPPERS (Conversión entre Capas)
    // =============================================================

    /**
     * Mapper específico para transformar registros de censura (WARNINGS) 
     * al modelo de dominio del Chat.
     */
    private ChatMessage warningToDomain(WarningDocument doc) {
        if (doc == null) return null;
        return ChatMessage.builder()
                .id(doc.getId())
                .fightId(doc.getFightId()) 
                .userId(doc.getUserId())
                .username(doc.getUsername())
                .content(doc.getContent())  
                .source(doc.getSource())   
                .count(doc.getCount())
                .timestamp(doc.getTimestamp())
                .filtered(true)  
                .flagged(true)    
                .build();

    }

    private ChatMessageDocument toDocument(ChatMessage domain) {
        if (domain == null) return null;
        return ChatMessageDocument.builder()
                .id(domain.getId())
                .fightId(domain.getFightId())
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .content(domain.getContent())
                .source(domain.getSource()) 
                .timestamp(domain.getTimestamp())
                .filtered(domain.isFiltered())
                .flagged(domain.isFlagged())
                .build();
    }

    private ChatMessage toDomain(ChatMessageDocument doc) {
        if (doc == null) return null;
        return ChatMessage.builder()
                .id(doc.getId())
                .fightId(doc.getFightId())
                .userId(doc.getUserId())
                .username(doc.getUsername())
                .content(doc.getContent())  
                .source(doc.getSource())   
                .count(null)
                .timestamp(doc.getTimestamp()) 
                .filtered(doc.isFiltered())
                .flagged(doc.isFlagged())
                .build();
    }
}