package com.plazoleta.domain.exception;

public class RestaurantServiceUnavailableException extends RuntimeException {

    public RestaurantServiceUnavailableException() {
        super("No se pudo contactar al servicio de plazoleta");
    }
}
