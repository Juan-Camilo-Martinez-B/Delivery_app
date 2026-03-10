package com.delivery.delivery_app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.delivery_app.dto.ItemPedidoRequest;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger log = Logger.getLogger(PedidoController.class.getName());

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Pedido> crearPedido(
            @PathVariable String usuarioId,
            @Valid @RequestBody List<ItemPedidoRequest> items) {
        log.info("POST /api/pedidos/" + usuarioId + " - Creando pedido con " + items.size() + " items");
        Pedido pedido = pedidoService.crearPedido(usuarioId, items);
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable String pedidoId) {
        log.info("GET /api/pedidos/" + pedidoId + " - Obteniendo información del pedido");
        Pedido pedido = pedidoService.obtenerPedido(pedidoId);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @GetMapping("/{pedidoId}/factura")
    public ResponseEntity<String> generarFactura(@PathVariable String pedidoId) {
        log.info("GET /api/pedidos/" + pedidoId + "/factura - Generando factura");
        String factura = pedidoService.generarFactura(pedidoId);
        return new ResponseEntity<>(factura, HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorEstado(@PathVariable String estado) {
        log.info("GET /api/pedidos/estado/" + estado + " - Buscando pedidos por estado");
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorEstado(estado);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("GET /api/pedidos/rango-fechas - Buscando pedidos entre " + inicio + " y " + fin);
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorRangoFechas(inicio, fin);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PutMapping("/{pedidoId}/estado/{nuevoEstado}")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable String pedidoId,
            @PathVariable String nuevoEstado) {
        log.info("PUT /api/pedidos/" + pedidoId + "/estado/" + nuevoEstado + " - Actualizando estado del pedido");
        Pedido pedido = pedidoService.actualizarEstado(pedidoId, nuevoEstado);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }
}
