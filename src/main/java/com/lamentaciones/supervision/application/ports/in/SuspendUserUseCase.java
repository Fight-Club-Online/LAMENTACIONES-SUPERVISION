package com.lamentaciones.supervision.application.ports.in;

import com.lamentaciones.supervision.application.commands.SuspendUserCommand;
import com.lamentaciones.supervision.domain.model.UserBan;

public interface SuspendUserUseCase {
    UserBan suspendUser(SuspendUserCommand command);
}