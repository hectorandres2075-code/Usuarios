package com.plazoleta.application.service;

import com.plazoleta.application.port.input.GetUserPhoneUseCase;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserNotFoundException;

public class GetUserPhoneService implements GetUserPhoneUseCase {

    private final UserPersistencePort userPersistencePort;

    public GetUserPhoneService(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public String getPhone(Long userId) {
        return userPersistencePort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId))
                .getCelular();
    }
}
