package com.delivery.delivery_app.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.model.Pago;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Tienda;
import com.delivery.delivery_app.repository.PagoRepository;
import com.delivery.delivery_app.repository.PedidoRepository;
import com.delivery.delivery_app.repository.ProductoRepository;

@Service
public class TiendaService {

    private static final Logger log = Logger.getLogger(TiendaService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

    @Transactional
    public Tienda crearTienda(Tienda tienda) {
        log.info("Creando nueva tienda: " + tienda.getNombre());

        if (tienda.getId() == null) {
            tienda.setId(UUID.randomUUID().toString());
        }

        entityManager.persist(tienda);
        log.info("Tienda creada exitosamente con ID: " + tienda.getId());

        return tienda;
    }

    public Tienda obtenerTiendaPorId(String tiendaId) {
        log.fine("Buscando tienda con ID: " + tiendaId);

        Tienda tienda = entityManager.find(Tienda.class, tiendaId);
        if (tienda == null) {
            log.severe("Tienda no encontrada con ID: " + tiendaId);
            throw new RuntimeException("Tienda no encontrada con ID: " + tiendaId);
        }

        return tienda;
    }

    @Transactional
    public Tienda actualizarDatosTienda(String tiendaId, Tienda datos) {
        log.info("Actualizando datos de tienda ID: " + tiendaId);

        Tienda tienda = obtenerTiendaPorId(tiendaId);
        
        if (datos.getNombre() != null && !datos.getNombre().trim().isEmpty()) {
            tienda.setNombre(datos.getNombre());
        }
        if (datos.getTelefono() != null && !datos.getTelefono().trim().isEmpty()) {
            tienda.setTelefono(datos.getTelefono());
        }
        if (datos.getDireccion() != null && !datos.getDireccion().trim().isEmpty()) {
            tienda.setDireccion(datos.getDireccion());
        }

        if (datos.getHorarioApertura() != null) {
            tienda.setHorarioApertura(datos.getHorarioApertura());
        }
        if (datos.getHorarioCierre() != null) {
            tienda.setHorarioCierre(datos.getHorarioCierre());
        }

        log.info("Datos de tienda actualizados exitosamente");
        return tienda;
    }

    public List<Producto> verProductosDeTienda(String tiendaId) {
        log.fine("Obteniendo productos de tienda ID: " + tiendaId);

        Tienda tienda = obtenerTiendaPorId(tiendaId);
        List<Producto> productos = tienda.getProductos();

        log.fine("Se encontraron " + productos.size() + " productos en la tienda");
        return productos;
    }

    @Transactional
    public Producto agregarProducto(String tiendaId, Producto producto) {
        log.info("Agregando producto a tienda ID: " + tiendaId);

        Tienda tienda = obtenerTiendaPorId(tiendaId);

        if (producto.getId() == null) {
            producto.setId(UUID.randomUUID().toString());
        }

        producto.setTienda(tienda);
        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto agregado exitosamente. ID: " + productoGuardado.getId() + 
                ", Nombre: " + productoGuardado.getNombre());

        return productoGuardado;
    }

    @Transactional
    public Producto actualizarProducto(String productoId, Producto datos) {
        log.info("Actualizando producto ID: " + productoId);

        Producto existente = productoRepository.findById(productoId)
                .orElseThrow(() -> {
                    log.severe("Producto no encontrado con ID: " + productoId);
                    return new RuntimeException("Producto no encontrado con ID: " + productoId);
                });

        if (datos.getNombre() != null && !datos.getNombre().trim().isEmpty()) {
            existente.setNombre(datos.getNombre());
        }
        if (datos.getPrecio() != null && datos.getPrecio() >= 0) {
            existente.setPrecio(datos.getPrecio());
        }
        if (datos.getCategoria() != null) {
            existente.setCategoria(datos.getCategoria());
        }
        if (datos.getDisponible() != null) {
            existente.setDisponible(datos.getDisponible());
        }
        if (datos.getDescripcion() != null) {
            existente.setDescripcion(datos.getDescripcion());
        }

        Producto productoActualizado = productoRepository.save(existente);
        log.info("Producto actualizado exitosamente");

        return productoActualizado;
    }

    public List<Pedido> verPedidosPendientes() {
        log.fine("Obteniendo pedidos pendientes");

        List<Pedido> pedidos = pedidoRepository.findByEstado("PENDIENTE");
        log.fine("Se encontraron " + pedidos.size() + " pedidos pendientes");

        return pedidos;
    }

    @Transactional
    public Pedido marcarPedidoListo(String pedidoId) {
        log.info("Marcando pedido como listo. ID: " + pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });

        // Validar que el pedido esté pagado
        boolean estaPagado = pagoRepository.findByPedido(pedido).stream().anyMatch(Pago::getCompletado);
        if (!estaPagado) {
            throw new RuntimeException("El pedido debe estar pagado antes de marcarlo como listo");
        }

        if (!"PAGADO".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido no está en estado PAGADO. Estado actual: " + pedido.getEstado());
        }

        pedido.setEstado("LISTO");
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        log.info("Pedido " + pedidoId + " marcado como listo exitosamente");
        return pedidoActualizado;
    }

    @Transactional
    public void eliminarProducto(String productoId) {
        log.info("Eliminando producto ID: " + productoId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> {
                    log.severe("Producto no encontrado con ID: " + productoId);
                    return new RuntimeException("Producto no encontrado con ID: " + productoId);
                });

        productoRepository.delete(producto);
        log.info("Producto eliminado exitosamente");
    }
}
