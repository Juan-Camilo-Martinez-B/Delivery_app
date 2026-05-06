package com.delivery.delivery_app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Usuario {
    @Id
    @Column(nullable = false)
    @JsonProperty("id")
    private String id;
    
    @Column(nullable = false)
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("direccion")
    private String direccion;
    
    public Usuario() {}
    
    public Usuario(String id, String nombre, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public void actualizarDatos(String nombre, String telefono, String direccion) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        }
        if (telefono != null && !telefono.trim().isEmpty()) {
            this.telefono = telefono;
        }
        if (direccion != null && !direccion.trim().isEmpty()) {
            this.direccion = direccion;
        }
    }
}
