package com.plazoleta.application.service;

import com.plazoleta.application.port.input.CreateEmployeeUseCase;
import com.plazoleta.application.port.output.AuthenticatedUserPort;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.RestaurantServicePort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidUserDataException;
import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;

public class CreateEmployeeService implements CreateEmployeeUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final RestaurantServicePort restaurantServicePort;
    private final AuthenticatedUserPort authenticatedUserPort;

    public CreateEmployeeService(UserPersistencePort userPersistencePort,
                                 PasswordEncoderPort passwordEncoderPort,
                                 RestaurantServicePort restaurantServicePort,
                                 AuthenticatedUserPort authenticatedUserPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.restaurantServicePort = restaurantServicePort;
        this.authenticatedUserPort = authenticatedUserPort;
    }

    @Override
    public User createEmployee(User employee) {
        Long ownerId = restaurantServicePort.findOwnerIdByRestaurantId(employee.getIdRestaurante())
                .orElseThrow(() -> new InvalidUserDataException("El restaurante indicado no existe"));
        // Solo el propietario de ESE restaurante puede crearle empleados.
        if (!ownerId.equals(authenticatedUserPort.currentUserId())) {
            throw new NotRestaurantOwnerException();
        }

        if (userPersistencePort.existsByCorreo(employee.getCorreo())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese correo");
        }
        if (userPersistencePort.existsByDocumento(employee.getDocumento())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con ese documento");
        }

        employee.setRol(Role.EMPLEADO);
        employee.setClave(passwordEncoderPort.encode(employee.getClave()));
        return userPersistencePort.save(employee);
    }
}
