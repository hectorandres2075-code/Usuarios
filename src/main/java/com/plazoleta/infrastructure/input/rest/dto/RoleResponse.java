package com.plazoleta.infrastructure.input.rest.dto;

public class RoleResponse {

    private final String rol;

    public RoleResponse(String rol) {
        this.rol = rol;
    }

    public String getRol() { return rol; }
}
