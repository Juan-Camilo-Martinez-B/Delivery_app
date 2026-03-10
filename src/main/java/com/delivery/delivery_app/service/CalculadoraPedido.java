package com.delivery.delivery_app.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.delivery.delivery_app.model.ItemPedido;
import com.delivery.delivery_app.model.Pedido;

@Component
public class CalculadoraPedido {
    
    private static final Logger log = Logger.getLogger(CalculadoraPedido.class.getName());
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    
    /**
     * Calcula el total de un pedido sumando el subtotal de cada item
     */
    public Double calcularTotal(Pedido pedido) {
        if (pedido == null || pedido.getItems() == null || pedido.getItems().isEmpty()) {
            log.fine("Pedido vacío o nulo, total = 0.0");
            return 0.0;
        }
        
        Double total = pedido.getItems().stream()
                .mapToDouble(this::calcularSubtotal)
                .sum();
        
        log.fine("Total calculado para pedido " + pedido.getId() + ": " + total);
        return Double.parseDouble(DECIMAL_FORMAT.format(total));
    }
    
    /**
     * Calcula el subtotal de un item
     */
    public Double calcularSubtotal(ItemPedido item) {
        if (item == null || item.getCantidad() == null || item.getPrecioUnitario() == null) {
            return 0.0;
        }
        return item.getCantidad() * item.getPrecioUnitario();
    }
    
    /**
     * Calcula el total con impuesto incluido
     */
    public Double calcularTotalConImpuesto(Pedido pedido, Double porcentajeImpuesto) {
        Double subtotal = calcularTotal(pedido);
        Double impuesto = subtotal * porcentajeImpuesto / 100;
        Double total = subtotal + impuesto;
        
        log.fine("Subtotal: " + subtotal + ", Impuesto (" + porcentajeImpuesto + "%): " + impuesto + ", Total: " + total);
        
        return Double.parseDouble(DECIMAL_FORMAT.format(total));
    }
    
    /**
     * Calcula el total de una lista de items
     */
    public Double calcularTotalItems(List<ItemPedido> items) {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        
        return items.stream()
                .mapToDouble(this::calcularSubtotal)
                .sum();
    }
    
    /**
     * Aplica un descuento al total
     */
    public Double aplicarDescuento(Double total, Double porcentajeDescuento) {
        if (porcentajeDescuento == null || porcentajeDescuento <= 0) {
            return total;
        }
        
        Double descuento = total * porcentajeDescuento / 100;
        Double totalConDescuento = total - descuento;
        
        log.fine("Total original: " + total + ", Descuento (" + porcentajeDescuento + "%): " + descuento + ", Total con descuento: " + totalConDescuento);
        
        return Double.parseDouble(DECIMAL_FORMAT.format(totalConDescuento));
    }
    
    /**
     * Calcula el vuelto a devolver
     */
    public Double calcularVuelto(Double montoPagado, Double total) {
        if (montoPagado < total) {
            return 0.0;
        }
        return Double.parseDouble(DECIMAL_FORMAT.format(montoPagado - total));
    }
}
