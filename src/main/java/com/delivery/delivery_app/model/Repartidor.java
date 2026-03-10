package com.delivery.delivery_app.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "repartidores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Repartidor extends Usuario {
    
    private Boolean disponible = true;
    private String vehiculo;
    private Integer entregasRealizadas = 0;
    
    public Repartidor() {
        super();
    }
    
    public Repartidor(String id, String nombre, String telefono, String direccion, Boolean disponible, String vehiculo) {
        super(id, nombre, telefono, direccion);
        this.disponible = disponible;
        this.vehiculo = vehiculo;
    }
    
    // Getters y Setters
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    
    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }
    
    public Integer getEntregasRealizadas() { return entregasRealizadas; }
    public void setEntregasRealizadas(Integer entregasRealizadas) { this.entregasRealizadas = entregasRealizadas; }
}
