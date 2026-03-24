package com.delivery.delivery_app.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @Column(nullable = false)
    private String id;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false)
    private Double total;
    
    @Column
    private String direccionEntrega;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id")
    @JsonIgnoreProperties({ "productos", "hibernateLazyInitializer", "handler" })
    private Tienda tienda;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> items = new ArrayList<>();
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();
    
    public Pedido() {}
    
    public Pedido(String id, LocalDateTime fecha, String estado, Double total) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
    }
    
    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
        if (estado == null) {
            estado = "PENDIENTE";
        }
        if (total == null) {
            total = 0.0;
        }
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Tienda getTienda() { return tienda; }
    public void setTienda(Tienda tienda) { this.tienda = tienda; }
    
    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }
    
    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
    
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
}
