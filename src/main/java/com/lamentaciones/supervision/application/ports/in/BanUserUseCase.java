package com.lamentaciones.supervision.application.ports.in;

import com.lamentaciones.supervision.application.commands.BanUserCommand;
import com.lamentaciones.supervision.domain.model.UserBan;

public interface BanUserUseCase {
    UserBan banUser(BanUserCommand command);
}