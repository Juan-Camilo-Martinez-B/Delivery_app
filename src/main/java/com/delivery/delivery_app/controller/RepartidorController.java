package com.delivery.delivery_app.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Repartidor;
import com.delivery.delivery_app.service.RepartidorService;

@RestController
@RequestMapping("/api/usuarios/repartidores")
public class RepartidorController {

    private static final Logger log = Logger.getLogger(RepartidorController.class.getName());

    @Autowired
    private RepartidorService repartidorService;

    @PutMapping("/{repartidorId}/pedidos/{pedidoId}/aceptar")
    public ResponseEntity<Pedido> aceptarPedido(
            @PathVariable String repartidorId,
            @PathVariable String pedidoId) {
        log.info("PUT /api/repartidores/" + repartidorId + "/pedidos/" + pedidoId + "/aceptar - Aceptando pedido");
        Pedido pedido = repartidorService.aceptarPedido(repartidorId, pedidoId);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @PutMapping("/pedidos/{pedidoId}/entregado")
    public ResponseEntity<Pedido> marcarComoEntregado(@PathVariable String pedidoId) {
        log.info("PUT /api/repartidores/pedidos/" + pedidoId + "/entregado - Marcando pedido como entregado");
        Pedido pedido = repartidorService.marcarComoEntregado(pedidoId);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @GetMapping("/pedidos/disponibles")
    public ResponseEntity<List<Pedido>> verPedidosDisponibles() {
        log.info("GET /api/repartidores/pedidos/disponibles - Obteniendo pedidos disponibles");
        List<Pedido> pedidos = repartidorService.verPedidosDisponibles();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/{repartidorId}/pedidos-asignados")
    public ResponseEntity<List<Pedido>> verPedidosAsignados(@PathVariable String repartidorId) {
        log.info("GET /api/repartidores/" + repartidorId + "/pedidos-asignados - Obteniendo pedidos asignados");
        List<Pedido> pedidos = repartidorService.verPedidosAsignados(repartidorId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PutMapping("/{repartidorId}/disponibilidad/{disponible}")
    public ResponseEntity<Repartidor> actualizarDisponibilidad(
            @PathVariable String repartidorId,
            @PathVariable Boolean disponible) {
        log.info("PUT /api/repartidores/" + repartidorId + "/disponibilidad/" + disponible + " - Actualizando disponibilidad");
        Repartidor repartidor = repartidorService.actualizarDisponibilidad(repartidorId, disponible);
        return new ResponseEntity<>(repartidor, HttpStatus.OK);
    }
}
