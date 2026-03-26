package com.delivery.delivery_app.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Tienda;
import com.delivery.delivery_app.service.TiendaService;

@RestController
@RequestMapping("/api/usuarios/tiendas")
public class TiendaController {

    private static final Logger log = Logger.getLogger(TiendaController.class.getName());

    @Autowired
    private TiendaService tiendaService;

    @PostMapping
    public ResponseEntity<Tienda> crearTienda(@Valid @RequestBody Tienda tienda) {
        log.info("POST /api/clientes/tiendas - Creando nueva tienda: " + tienda.getNombre());
        Tienda nuevaTienda = tiendaService.crearTienda(tienda);
        return new ResponseEntity<>(nuevaTienda, HttpStatus.CREATED);
    }

    @PutMapping("/{tiendaId}")
    public ResponseEntity<Tienda> actualizarDatos(
            @PathVariable String tiendaId,
            @Valid @RequestBody Tienda tienda) {
        log.info("PUT /api/clientes/tiendas/" + tiendaId + " - Actualizando datos de tienda");
        Tienda tiendaActualizada = tiendaService.actualizarDatosTienda(tiendaId, tienda);
        return new ResponseEntity<>(tiendaActualizada, HttpStatus.OK);
    }

    @GetMapping("/{tiendaId}/productos")
    public ResponseEntity<List<Producto>> verProductos(@PathVariable String tiendaId) {
        log.info("GET /api/clientes/tiendas/" + tiendaId + "/productos - Obteniendo productos de la tienda");
        List<Producto> productos = tiendaService.verProductosDeTienda(tiendaId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PostMapping("/{tiendaId}/productos")
    public ResponseEntity<Producto> agregarProducto(
            @PathVariable String tiendaId,
            @Valid @RequestBody Producto producto) {
        log.info("POST /api/clientes/tiendas/" + tiendaId + "/productos - Agregando nuevo producto");
        Producto nuevoProducto = tiendaService.agregarProducto(tiendaId, producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/productos/{productoId}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable String productoId,
            @Valid @RequestBody Producto producto) {
        log.info("PUT /api/clientes/tiendas/productos/" + productoId + " - Actualizando producto");
        Producto productoActualizado = tiendaService.actualizarProducto(productoId, producto);
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/productos/{productoId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String productoId) {
        log.info("DELETE /api/clientes/tiendas/productos/" + productoId + " - Eliminando producto");
        tiendaService.eliminarProducto(productoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/pedidos/pendientes")
    public ResponseEntity<List<Pedido>> verPedidosPendientes() {
        log.info("GET /api/clientes/tiendas/pedidos/pendientes - Obteniendo pedidos pendientes");
        List<Pedido> pedidos = tiendaService.verPedidosPendientes();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PutMapping("/pedidos/{pedidoId}/listo")
    public ResponseEntity<Pedido> marcarPedidoListo(@PathVariable String pedidoId) {
        log.info("PUT /api/clientes/tiendas/pedidos/" + pedidoId + "/listo - Marcando pedido como listo");
        Pedido pedido = tiendaService.marcarPedidoListo(pedidoId);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }
}
