package com.delivery.delivery_app.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario {
    
    private Integer puntosFidelidad = 0;
    private String preferencias;
    
    public Cliente() {
        super();
    }
    
    public Cliente(String id, String nombre, String telefono, String direccion) {
        super(id, nombre, telefono, direccion);
    }
    
    // Getters y Setters
    public Integer getPuntosFidelidad() { return puntosFidelidad; }
    public void setPuntosFidelidad(Integer puntosFidelidad) { this.puntosFidelidad = puntosFidelidad; }
    
    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }
}
