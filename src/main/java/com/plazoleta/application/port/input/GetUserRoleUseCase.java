package com.plazoleta.application.port.input;

import com.plazoleta.domain.model.Role;

public interface GetUserRoleUseCase {

    Role getRole(Long userId);
}
