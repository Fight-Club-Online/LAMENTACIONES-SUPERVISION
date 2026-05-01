package com.lamentaciones.supervision.infrastructure.persistence.mongo.adapters;

import com.lamentaciones.supervision.domain.enums.ReportStatus;
import com.lamentaciones.supervision.domain.model.Report;
import com.lamentaciones.supervision.domain.repository.ReportRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.repositories.ReportMongoRepository;
import com.lamentaciones.supervision.infrastructure.persistence.mongo.documents.ReportDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MongoReportAdapter implements ReportRepository {

    private final ReportMongoRepository mongoRepo;

    public MongoReportAdapter(ReportMongoRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    @Override
    public Report save(Report report) {
        ReportDocument doc = toDocument(report);
        return toDomain(mongoRepo.save(doc));
    }

    @Override
    public Optional<Report> findById(String id) {
        return mongoRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Report> findByStatus(ReportStatus status, int skip, int limit) {
        int page = limit > 0 ? skip / limit : 0;
        return mongoRepo.findByReportStatusWithFallback(status.name(), PageRequest.of(page, limit))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByReportedUserId(String userId) {
        return mongoRepo.findByReportedUserId(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findAll(int page, int size) {
        return mongoRepo.findAll(PageRequest.of(page, size))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private ReportDocument toDocument(Report domain) {
        if (domain == null)
            return null;
        return ReportDocument.builder()
                .id(domain.getId())
                .reportedUserId(domain.getReportedUserId())
                .reporterId(domain.getReporterId())
                .reporterUsername(domain.getReporterUsername())
                .reportedUsername(domain.getReportedUsername())
                .description(domain.getDescription())
                .fightId(domain.getFightId())
                .evidenceMessages(domain.getEvidenceMessages())
                .reportStatus(domain.getReportStatus() != null ? domain.getReportStatus().name() : null)
                .adminNotes(domain.getAdminNotes())
                .createdAt(domain.getCreatedAt())
                .resolvedAt(domain.getResolvedAt())
                .build();
    }

    private Report toDomain(ReportDocument doc) {
        if (doc == null)
            return null;
        return Report.builder()
                .id(doc.getId())
                .reportedUserId(doc.getReportedUserId())
                .reporterId(doc.getReporterId())
                .reporterUsername(doc.getReporterUsername())
                .reportedUsername(doc.getReportedUsername())
                .description(doc.getDescription())
                .fightId(doc.getFightId())
                .evidenceMessages(doc.getEvidenceMessages())
                .reportStatus(doc.getReportStatus() != null
                        ? ReportStatus.valueOf(doc.getReportStatus())
                        : ReportStatus.PENDING)
                .adminNotes(doc.getAdminNotes())
                .createdAt(doc.getCreatedAt())
                .resolvedAt(doc.getResolvedAt())
                .build();
    }
}