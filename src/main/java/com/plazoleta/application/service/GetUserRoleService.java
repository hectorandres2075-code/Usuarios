package com.plazoleta.application.service;

import com.plazoleta.application.port.input.GetUserRoleUseCase;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserNotFoundException;
import com.plazoleta.domain.model.Role;

public class GetUserRoleService implements GetUserRoleUseCase {

    private final UserPersistencePort userPersistencePort;

    public GetUserRoleService(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public Role getRole(Long userId) {
        return userPersistencePort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId))
                .getRol();
    }
}
