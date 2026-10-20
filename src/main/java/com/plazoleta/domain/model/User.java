package com.plazoleta.domain.model;

import java.time.LocalDate;

/** Modelo de dominio: clase Java pura, sin anotaciones de Spring ni JPA. */
public class User {

    private Long id;
    private String nombre;
    private String apellido;
    private String documento;
    private String celular;
    private LocalDate fechaNacimiento;
    private String correo;
    private String clave;
    private Role rol;
    private Long idRestaurante; // solo se usa en los EMPLEADOS

    public User() {
    }

    public User(Long id, String nombre, String apellido, String documento, String celular,
                LocalDate fechaNacimiento, String correo, String clave, Role rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }
    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }
}
