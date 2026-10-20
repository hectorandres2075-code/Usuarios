package com.plazoleta.application.port.output;

import com.plazoleta.domain.model.User;

import java.util.Optional;

public interface UserPersistencePort {

    Optional<User> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    User save(User user);
}
