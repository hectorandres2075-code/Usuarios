package com.plazoleta.application.port.input;

import com.plazoleta.domain.model.User;

public interface CreateClientUseCase {

    /** Registra un usuario con rol CLIENTE. */
    User createClient(User client);
}
