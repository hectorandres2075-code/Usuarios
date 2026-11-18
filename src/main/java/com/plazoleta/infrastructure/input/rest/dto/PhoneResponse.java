package com.plazoleta.infrastructure.input.rest.dto;

public class PhoneResponse {

    private final String celular;

    public PhoneResponse(String celular) {
        this.celular = celular;
    }

    public String getCelular() { return celular; }
}
