package com.delivery.delivery_app.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "devoluciones")
public class Devolucion extends Transaccion {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago;
    
    private String motivo;
    
    public Devolucion() {}
    
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
