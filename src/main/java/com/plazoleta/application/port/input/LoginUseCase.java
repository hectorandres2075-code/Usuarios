package com.plazoleta.application.port.input;

public interface LoginUseCase {

    /** Valida las credenciales y devuelve un token JWT. */
    String login(String correo, String clave);
}
