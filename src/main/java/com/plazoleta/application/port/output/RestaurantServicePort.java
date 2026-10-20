package com.plazoleta.application.port.output;

import java.util.Optional;

/** Lo que Usuarios necesita del microservicio de Plazoleta. */
public interface RestaurantServicePort {

    /** Devuelve el id del propietario del restaurante, o vacío si el restaurante no existe. */
    Optional<Long> findOwnerIdByRestaurantId(Long restaurantId);
}
