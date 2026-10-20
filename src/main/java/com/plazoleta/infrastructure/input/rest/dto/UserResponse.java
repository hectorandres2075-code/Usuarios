package com.plazoleta.infrastructure.input.rest.dto;

/** Respuesta pública de un usuario: nunca incluye la clave. */
public class UserResponse {

    private final Long id;
    private final String nombre;
    private final String apellido;
    private final String correo;
    private final String rol;

    public UserResponse(Long id, String nombre, String apellido, String correo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
}
