package com.delivery.delivery_app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Usuario {
    
    @Id
    @Column(nullable = false)
    private String id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column
    private String telefono;
    
    @Column
    private String direccion;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();
    
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
    
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    
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
    
    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setUsuario(this);
    }
}
