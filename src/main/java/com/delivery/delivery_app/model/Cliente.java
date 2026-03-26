package com.delivery.delivery_app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario {
    
    @Column(nullable = false)
    private Integer puntosFidelidad = 0;
    
    @Column
    private String preferencias;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();
    
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
    
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    
    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setCliente(this);
    }
}
