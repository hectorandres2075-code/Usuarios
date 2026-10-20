package com.plazoleta.application.port.input;

import com.plazoleta.domain.model.User;

public interface CreateOwnerUseCase {

    /** Crea un usuario con rol PROPIETARIO. */
    User createOwner(User owner);
}
