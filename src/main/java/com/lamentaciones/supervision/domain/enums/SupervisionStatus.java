package com.lamentaciones.supervision.domain.enums;

public enum SupervisionStatus {
    ACTIVE,
    WARNING,       // tiene advertencias pero puede acceder
    SUSPENDED,     // suspendido hasta fecha específica
    BANNED         // bloqueado permanentemente
}