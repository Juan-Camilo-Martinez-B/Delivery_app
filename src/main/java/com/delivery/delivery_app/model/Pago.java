package com.delivery.delivery_app.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pagos")
public class Pago extends Transaccion {
    
    private String metodo;
    private Double montoRecibido;
    
    public Pago() {}
    
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    
    public Double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(Double montoRecibido) { this.montoRecibido = montoRecibido; }
}
