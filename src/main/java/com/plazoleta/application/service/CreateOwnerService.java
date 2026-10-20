package com.plazoleta.application.service;

import com.plazoleta.application.port.input.CreateOwnerUseCase;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidUserDataException;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;

import java.time.LocalDate;
import java.time.Period;

public class CreateOwnerService implements CreateOwnerUseCase {

    private static final int EDAD_MINIMA = 18;

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CreateOwnerService(UserPersistencePort userPersistencePort,
                              PasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public User createOwner(User owner) {
        if (!esMayorDeEdad(owner.getFechaNacimiento())) {
            throw new InvalidUserDataException("El propietario debe ser mayor de edad");
        }
        if (userPersistencePort.existsByCorreo(owner.getCorreo())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese correo");
        }
        if (userPersistencePort.existsByDocumento(owner.getDocumento())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese documento");
        }

        owner.setRol(Role.PROPIETARIO);
        owner.setClave(passwordEncoderPort.encode(owner.getClave()));
        return userPersistencePort.save(owner);
    }

    private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        return fechaNacimiento != null
                && Period.between(fechaNacimiento, LocalDate.now()).getYears() >= EDAD_MINIMA;
    }
}
