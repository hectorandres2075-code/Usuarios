package com.plazoleta.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Correo o clave incorrectos");
    }
}
