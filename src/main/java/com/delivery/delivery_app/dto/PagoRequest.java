package com.delivery.delivery_app.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PagoRequest {
    
    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;
    
    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto no puede ser negativo")
    private Double monto;
    
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodo;
    
    private String referencia;
    
    public PagoRequest() {}
    
    public PagoRequest(String pedidoId, Double monto, String metodo) {
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodo = metodo;
    }
    
    // Getters y Setters
    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }
    
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
