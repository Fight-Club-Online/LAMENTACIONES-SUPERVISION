package com.lamentaciones.supervision.application.ports.in;

import com.lamentaciones.supervision.application.dto.UserStatusResponse;

public interface CheckUserStatusUseCase {
    UserStatusResponse checkStatus(String userId);  // usado en cada login/token
}