package com.delivery.delivery_app.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Repartidor;
import com.delivery.delivery_app.repository.PedidoRepository;
import com.delivery.delivery_app.repository.UsuarioRepository;

@Service
public class RepartidorService {

    private static final Logger log = Logger.getLogger(RepartidorService.class.getName());

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Pedido aceptarPedido(String repartidorId, String pedidoId) {
        log.info("Repartidor " + repartidorId + " aceptando pedido ID: " + pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });

        Repartidor repartidor = (Repartidor) usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> {
                    log.severe("Repartidor no encontrado con ID: " + repartidorId);
                    return new RuntimeException("Repartidor no encontrado con ID: " + repartidorId);
                });

        // Validar que el pedido esté listo para ser aceptado
        if (!"LISTO".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido no está listo para ser aceptado. Estado actual: " + pedido.getEstado());
        }

        pedido.setRepartidor(repartidor);
        pedido.setEstado("EN_CAMINO");
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        log.info("Pedido " + pedidoId + " aceptado por repartidor " + repartidorId + ". Nuevo estado: EN_CAMINO");
        return pedidoActualizado;
    }

    @Transactional
    public Pedido marcarComoEntregado(String pedidoId) {
        log.info("Marcando pedido como entregado. ID: " + pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });

        // Validar que el pedido esté en camino
        if (!"EN_CAMINO".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido no está en camino. Estado actual: " + pedido.getEstado());
        }

        pedido.setEstado("ENTREGADO");
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        log.info("Pedido " + pedidoId + " marcado como entregado exitosamente");
        return pedidoActualizado;
    }

    public List<Pedido> verPedidosDisponibles() {
        log.fine("Buscando pedidos disponibles para repartidor");

        List<Pedido> pedidosDisponibles = pedidoRepository.findByEstado("LISTO");
        log.fine("Se encontraron " + pedidosDisponibles.size() + " pedidos disponibles");

        return pedidosDisponibles;
    }

    public List<Pedido> verPedidosAsignados(String repartidorId) {
        log.fine("Buscando pedidos asignados al repartidor ID: " + repartidorId);

        // Aquí iría la lógica para filtrar por repartidor específico
        List<Pedido> pedidosAsignados = pedidoRepository.findByEstado("EN_CAMINO");
        
        log.fine("Se encontraron " + pedidosAsignados.size() + " pedidos asignados");
        return pedidosAsignados;
    }

    @Transactional
    public Repartidor actualizarDisponibilidad(String repartidorId, Boolean disponible) {
        log.info("Actualizando disponibilidad del repartidor " + repartidorId + " a " + disponible);

        Repartidor repartidor = (Repartidor) usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> {
                    log.severe("Repartidor no encontrado con ID: " + repartidorId);
                    return new RuntimeException("Repartidor no encontrado con ID: " + repartidorId);
                });

        repartidor.setDisponible(disponible);
        Repartidor repartidorActualizado = (Repartidor) usuarioRepository.save(repartidor);

        log.info("Disponibilidad del repartidor actualizada exitosamente");
        return repartidorActualizado;
    }
}
