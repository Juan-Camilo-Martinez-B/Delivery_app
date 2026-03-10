package com.delivery.delivery_app.service;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.dto.PagoRequest;
import com.delivery.delivery_app.model.Devolucion;
import com.delivery.delivery_app.model.Pago;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.repository.DevolucionRepository;
import com.delivery.delivery_app.repository.PagoRepository;
import com.delivery.delivery_app.repository.PedidoRepository;

@Service
public class PagoService {

    private static final Logger log = Logger.getLogger(PagoService.class.getName());

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private DevolucionRepository devolucionRepository;
    
    @Autowired
    private CalculadoraPedido calculadoraPedido;

    @Transactional
    public Pago procesarPago(PagoRequest request) {
        log.info("Procesando pago para pedido ID: " + request.getPedidoId());

        Pago transaccion = crearTransaccionPago(request);
        return procesarTransaccionPago(transaccion.getId());
    }
    
    @Transactional
    public Pago crearTransaccionPago(PagoRequest request) {
        if (request.getMonto() <= 0) {
            throw new RuntimeException("El monto del pago debe ser mayor a cero");
        }
        
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + request.getPedidoId());
                    return new RuntimeException("Pedido no encontrado con ID: " + request.getPedidoId());
                });
        
        Double totalPedido = calculadoraPedido.calcularTotal(pedido);
        
        Pago pago = new Pago();
        pago.setId(UUID.randomUUID().toString());
        pago.setPedido(pedido);
        pago.setMonto(totalPedido);
        pago.setMontoRecibido(request.getMonto());
        pago.setMetodo(request.getMetodo());
        pago.setReferencia(request.getReferencia());
        pago.setCompletado(false);
        
        return pagoRepository.save(pago);
    }
    
    @Transactional
    public Pago procesarTransaccionPago(String pagoId) {
        Pago pago = obtenerPago(pagoId);
        
        if (Boolean.TRUE.equals(pago.getCompletado())) {
            throw new RuntimeException("La transacción de pago ya fue procesada");
        }
        
        Pedido pedido = pago.getPedido();
        boolean existePagoCompletado = pagoRepository.findByPedido(pedido).stream()
                .anyMatch(p -> Boolean.TRUE.equals(p.getCompletado()) && !p.getId().equals(pagoId));
        
        if (existePagoCompletado) {
            throw new RuntimeException("El pedido ya ha sido pagado");
        }
        
        Double montoRecibido = pago.getMontoRecibido() != null ? pago.getMontoRecibido() : pago.getMonto();
        if (montoRecibido < pago.getMonto()) {
            throw new RuntimeException(String.format(
                    "Monto insuficiente. Total del pedido: $%.2f, Monto proporcionado: $%.2f",
                    pago.getMonto(), montoRecibido));
        }
        
        Double vuelto = calculadoraPedido.calcularVuelto(montoRecibido, pago.getMonto());
        if (vuelto > 0) {
            log.info("Vuelto a devolver: $" + vuelto);
        }
        
        pago.setCompletado(true);
        Pago pagoProcesado = pagoRepository.save(pago);
        
        if ("PENDIENTE".equals(pedido.getEstado())) {
            pedido.setEstado("PAGADO");
            pedidoRepository.save(pedido);
        }
        
        log.info("Pago procesado exitosamente. ID: " + pagoProcesado.getId() +
                ", Método: " + pagoProcesado.getMetodo() + ", Monto: " + pagoProcesado.getMonto());
        
        return pagoProcesado;
    }

    public Pago obtenerPago(String pagoId) {
        log.fine("Buscando pago con ID: " + pagoId);

        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> {
                    log.severe("Pago no encontrado con ID: " + pagoId);
                    return new RuntimeException("Pago no encontrado con ID: " + pagoId);
                });
    }

    @Transactional
    public Pago reembolsarPago(String pagoId) {
        log.info("Procesando reembolso para pago ID: " + pagoId);

        Pago pago = obtenerPago(pagoId);

        if (!pago.getCompletado()) {
            throw new RuntimeException("El pago no está completado, no se puede reembolsar");
        }

        Devolucion devolucion = new Devolucion();
        devolucion.setId(UUID.randomUUID().toString());
        devolucion.setPedido(pago.getPedido());
        devolucion.setPago(pago);
        devolucion.setMonto(pago.getMonto());
        devolucion.setReferencia(pago.getReferencia());
        devolucion.setCompletado(false);
        
        Devolucion transaccionDevolucion = devolucionRepository.save(devolucion);
        transaccionDevolucion.setCompletado(true);
        devolucionRepository.save(transaccionDevolucion);
        
        pago.setCompletado(false);
        
        // Actualizar estado del pedido
        Pedido pedido = pago.getPedido();
        pedido.setEstado("PENDIENTE");

        Pago pagoActualizado = pagoRepository.save(pago);
        pedidoRepository.save(pedido);

        log.info("Reembolso procesado exitosamente para pago ID: " + pagoId);

        return pagoActualizado;
    }
}
