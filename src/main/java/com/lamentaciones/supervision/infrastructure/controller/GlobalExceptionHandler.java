package com.lamentaciones.supervision.infrastructure.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
        log.error("[ERROR] Excepción capturada: {}", ex.getMessage(), ex);
        return ResponseEntity
                .internalServerError()
                .body(Map.of(
                        "error", ex.getClass().getSimpleName(),
                        "message", ex.getMessage() != null ? ex.getMessage() : "Sin mensaje"
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        log.warn("[WARN] IllegalState: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "IllegalStateException",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("[WARN] IllegalArgument: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "IllegalArgumentException",
                        "message", ex.getMessage()
                ));
    }
}