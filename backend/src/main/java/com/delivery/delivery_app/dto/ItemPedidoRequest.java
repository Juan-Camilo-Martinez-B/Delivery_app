package com.delivery.delivery_app.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ItemPedidoRequest {
    
    @NotBlank(message = "El ID del producto es obligatorio")
    private String productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;
    
    public ItemPedidoRequest() {}
    
    public ItemPedidoRequest(String productoId, Integer cantidad, Double precioUnitario) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
}
