package com.lamentaciones.supervision.domain.repository;

import java.util.List;
import java.util.Optional;

import com.lamentaciones.supervision.domain.enums.ReportStatus;
import com.lamentaciones.supervision.domain.model.Report;

/**
 * Puerto de salida para la gestión de denuncias (Reports).
 */
public interface ReportRepository {

    /**
     * Guarda o actualiza un reporte (usado principalmente para persistir la revisión del admin).
     */
    Report save(Report report);

    /**
     * Busca un reporte por su ID único.
     */
    Optional<Report> findById(String id);

    /**
     * Recupera reportes filtrados por su estado (ej. PENDING) con paginación técnica.
     */
    List<Report> findByStatus(ReportStatus status, int skip, int limit);

    /**
     * Obtiene todas las denuncias que se han hecho contra un usuario específico para historial.
     */
    List<Report> findByReportedUserId(String userId);
}