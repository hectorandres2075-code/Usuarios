package com.plazoleta.domain.exception;

public class NotRestaurantOwnerException extends RuntimeException {

    public NotRestaurantOwnerException() {
        super("Solo el propietario del restaurante puede crear empleados en él");
    }
}
