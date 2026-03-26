package com.delivery.delivery_app.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tiendas")
@PrimaryKeyJoinColumn(name = "usuario_id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Tienda extends Usuario {
    
    @Column
    private LocalTime horarioApertura;
    
    @Column
    private LocalTime horarioCierre;
    
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();
    
    public Tienda() {
        super();
    }
    
    public Tienda(String id, String nombre, String telefono, String direccion) {
        super(id, nombre, telefono, direccion);
    }
    
    // Getters y Setters
    public LocalTime getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(LocalTime horarioApertura) { this.horarioApertura = horarioApertura; }
    
    public LocalTime getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(LocalTime horarioCierre) { this.horarioCierre = horarioCierre; }
    
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}
