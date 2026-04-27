package com.lamentaciones.supervision.application.ports.in;

import com.lamentaciones.supervision.domain.model.UserBan;

public interface LiftBanUseCase {
    UserBan liftBan(String userId, String adminId);
}