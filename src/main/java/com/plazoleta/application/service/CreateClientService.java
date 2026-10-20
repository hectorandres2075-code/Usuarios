package com.plazoleta.application.service;

import com.plazoleta.application.port.input.CreateClientUseCase;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;

public class CreateClientService implements CreateClientUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CreateClientService(UserPersistencePort userPersistencePort,
                               PasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public User createClient(User client) {
        if (userPersistencePort.existsByCorreo(client.getCorreo())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese correo");
        }
        if (userPersistencePort.existsByDocumento(client.getDocumento())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese documento");
        }

        client.setRol(Role.CLIENTE); // el rol lo decide el sistema, nunca el que se registra
        client.setClave(passwordEncoderPort.encode(client.getClave()));
        return userPersistencePort.save(client);
    }
}
