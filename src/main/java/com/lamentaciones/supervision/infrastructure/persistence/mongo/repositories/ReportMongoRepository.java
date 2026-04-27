package com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.ReportDocument;

public interface ReportMongoRepository extends MongoRepository<ReportDocument, String> {
    List<ReportDocument> findByReportedUserId(String userId);

    List<ReportDocument> findByReportStatus(String status, Pageable pageable);

    long countByReportedUserIdAndReportStatus(String userId, String status);

    @Query("{ '$or': [ { 'reportStatus': ?0 }, { 'reportStatus': null }, { 'reportStatus': { '$exists': false } } ] }")
    Page<ReportDocument> findByReportStatusWithFallback(String status, Pageable pageable);
}