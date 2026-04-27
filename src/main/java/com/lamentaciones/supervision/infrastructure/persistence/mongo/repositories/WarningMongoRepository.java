package com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.WarningDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WarningMongoRepository extends MongoRepository<WarningDocument, String> {
    
    /**
     * Recupera todas las advertencias de censura ordenadas por la más reciente.
     * Ideal para el endpoint aislado de supervisión.
     */
    List<WarningDocument> findAllByOrderByTimestampDesc(Pageable pageable);

    /**
     * Permite filtrar las advertencias por un usuario específico para ver su reincidencia.
     */
    List<WarningDocument> findByUserIdOrderByTimestampDesc(String userId);
}