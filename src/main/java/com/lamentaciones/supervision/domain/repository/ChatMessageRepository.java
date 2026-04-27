package com.lamentaciones.supervision.domain.repository;

import java.util.List;

import com.lamentaciones.supervision.domain.model.ChatMessage;

/**
 * Puerto de salida para la persistencia de mensajes de chat.
 * Define cómo el dominio interactúa con el almacenamiento de logs de chat.
 */
public interface ChatMessageRepository {

    /**
     * Guarda un mensaje de chat para auditoría o historial.
     */
    ChatMessage save(ChatMessage message);

    /**
     * Recupera el historial de una pelea específica.
     * @param fightId ID de la pelea.
     * @param limit Cantidad de mensajes a recuperar.
     */
    List<ChatMessage> findByFightIdOrderByTimestamp(String fightId, int limit);

    /**
     * Recupera mensajes que han sido marcados (flagged) por el sistema automático.
     * @param skip Cantidad de registros a saltar (paginación).
     * @param limit Cantidad de registros a traer.
     */
    List<ChatMessage> findFlagged(int skip, int limit);
}