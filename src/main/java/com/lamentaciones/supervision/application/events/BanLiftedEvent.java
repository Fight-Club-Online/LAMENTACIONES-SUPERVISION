package com.lamentaciones.supervision.application.events;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BanLiftedEvent {
    private String userId;
    private String adminId;
}