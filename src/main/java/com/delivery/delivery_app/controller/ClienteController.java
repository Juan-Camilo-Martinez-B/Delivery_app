package com.delivery.delivery_app.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.delivery_app.dto.ItemPedidoRequest;
import com.delivery.delivery_app.model.Cliente;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.service.ClienteService;

@RestController
@RequestMapping("/api/usuarios/clientes")
public class ClienteController {

    private static final Logger log = Logger.getLogger(ClienteController.class.getName());

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        log.info("POST /api/clientes - Creando nuevo cliente: " + cliente.getNombre());
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String clienteId) {
        log.info("GET /api/clientes/" + clienteId + " - Obteniendo cliente");
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PostMapping("/{clienteId}/pedidos")
    public ResponseEntity<Pedido> realizarPedido(
            @PathVariable String clienteId,
            @Valid @RequestBody List<ItemPedidoRequest> items) {
        log.info("POST /api/clientes/" + clienteId + "/pedidos - Realizando pedido con " + items.size() + " items");
        Pedido nuevoPedido = clienteService.realizarPedido(clienteId, items);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/{clienteId}/historial")
    public ResponseEntity<List<Pedido>> verHistorial(@PathVariable String clienteId) {
        log.info("GET /api/clientes/" + clienteId + "/historial - Obteniendo historial de pedidos");
        List<Pedido> historial = clienteService.verHistorialPedidos(clienteId);
        return new ResponseEntity<>(historial, HttpStatus.OK);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        log.info("GET /api/clientes/productos - Obteniendo todos los productos");
        List<Producto> productos = clienteService.obtenerTodosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/productos/disponibles")
    public ResponseEntity<List<Producto>> buscarProductosDisponibles() {
        log.info("GET /api/clientes/productos/disponibles - Buscando productos disponibles");
        List<Producto> productos = clienteService.buscarProductosDisponibles();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarProductosPorCategoria(@PathVariable String categoria) {
        log.info("GET /api/clientes/productos/categoria/" + categoria + " - Buscando productos por categoría");
        List<Producto> productos = clienteService.buscarProductosPorCategoria(categoria);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
}
