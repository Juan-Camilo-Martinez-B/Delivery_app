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
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Usuario;
import com.delivery.delivery_app.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger log = Logger.getLogger(ClienteController.class.getName());

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        log.info("POST /api/clientes/usuarios - Creando nuevo usuario: " + usuario.getNombre());
        Usuario nuevoUsuario = clienteService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String usuarioId) {
        log.info("GET /api/clientes/usuarios/" + usuarioId + " - Obteniendo usuario");
        Usuario usuario = clienteService.obtenerUsuarioPorId(usuarioId);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping("/{usuarioId}/pedidos")
    public ResponseEntity<Pedido> realizarPedido(
            @PathVariable String usuarioId,
            @Valid @RequestBody List<ItemPedidoRequest> items) {
        log.info("POST /api/clientes/" + usuarioId + "/pedidos - Realizando pedido con " + items.size() + " items");
        Pedido nuevoPedido = clienteService.realizarPedido(usuarioId, items);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/{usuarioId}/historial")
    public ResponseEntity<List<Pedido>> verHistorial(@PathVariable String usuarioId) {
        log.info("GET /api/clientes/" + usuarioId + "/historial - Obteniendo historial de pedidos");
        List<Pedido> historial = clienteService.verHistorialPedidos(usuarioId);
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
