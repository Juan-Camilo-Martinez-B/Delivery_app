package com.delivery.delivery_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.dto.ItemPedidoRequest;
import com.delivery.delivery_app.model.ItemPedido;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Tienda;
import com.delivery.delivery_app.model.Cliente;
import com.delivery.delivery_app.repository.PedidoRepository;
import com.delivery.delivery_app.repository.ProductoRepository;
import com.delivery.delivery_app.repository.ClienteRepository;

@Service
public class PedidoService {

    private static final Logger log = Logger.getLogger(PedidoService.class.getName());

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CalculadoraPedido calculadoraPedido;

    @Transactional
    public Pedido crearPedido(String clienteId, List<ItemPedidoRequest> items) {
        log.info("Creando nuevo pedido para cliente ID: " + clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.severe("Cliente no encontrado con ID: " + clienteId);
                    return new RuntimeException("Cliente no encontrado con ID: " + clienteId);
                });

        Pedido pedido = new Pedido();
        pedido.setId(UUID.randomUUID().toString());
        pedido.setCliente(cliente);
        pedido.setEstado("PENDIENTE");
        pedido.setDireccionEntrega(cliente.getDireccion());

        Tienda tiendaDelPedido = null;
        for (ItemPedidoRequest itemRequest : items) {
            Producto producto = productoRepository.findById(itemRequest.getProductoId())
                    .orElseThrow(() -> {
                        log.severe("Producto no encontrado con ID: " + itemRequest.getProductoId());
                        return new RuntimeException("Producto no encontrado con ID: " + itemRequest.getProductoId());
                    });

            if (!producto.getDisponible()) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no está disponible");
            }

            if (producto.getTienda() == null) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no tiene tienda asociada");
            }

            if (tiendaDelPedido == null) {
                tiendaDelPedido = producto.getTienda();
            } else if (!tiendaDelPedido.getId().equals(producto.getTienda().getId())) {
                throw new RuntimeException("Todos los productos del pedido deben pertenecer a la misma tienda");
            }

            ItemPedido item = new ItemPedido();
            item.setId(UUID.randomUUID().toString());
            item.setProducto(producto);
            item.setCantidad(itemRequest.getCantidad());
            item.setPrecioUnitario(itemRequest.getPrecioUnitario());

            pedido.getItems().add(item);
            item.setPedido(pedido);
        }

        if (tiendaDelPedido == null) {
            throw new RuntimeException("El pedido debe contener al menos un producto");
        }
        pedido.setTienda(tiendaDelPedido);

        // Usar calculadora para el total
        Double total = calculadoraPedido.calcularTotal(pedido);
        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("Pedido creado exitosamente. ID: " + pedidoGuardado.getId() + ", Total: " + total);

        return pedidoGuardado;
    }

    public String generarFactura(String pedidoId) {
        log.info("Generando factura para pedido ID: " + pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });

        // Usar calculadora para asegurar total actualizado
        Double total = calculadoraPedido.calcularTotal(pedido);

        String itemsStr = pedido.getItems().stream()
                .map(i -> String.format("  - %s x%d @ $%.2f = $%.2f",
                        i.getProducto().getNombre(),
                        i.getCantidad(),
                        i.getPrecioUnitario(),
                        calculadoraPedido.calcularSubtotal(i)))
                .collect(Collectors.joining("\n"));

        String factura = String.format(
                "=====================================\n" +
                "           FACTURA DE PEDIDO         \n" +
                "=====================================\n" +
                "Pedido ID: %s\n" +
                "Fecha: %s\n" +
                "Cliente: %s\n" +
                "Teléfono: %s\n" +
                "Dirección: %s\n" +
                "-------------------------------------\n" +
                "DETALLE DE PRODUCTOS:\n%s\n" +
                "-------------------------------------\n" +
                "TOTAL A PAGAR: $%.2f\n" +
                "=====================================",
                pedido.getId(),
                pedido.getFecha().toString(),
                pedido.getCliente().getNombre(),
                pedido.getCliente().getTelefono(),
                pedido.getDireccionEntrega(),
                itemsStr,
                total
        );

        log.info("Factura generada exitosamente para pedido ID: " + pedidoId);
        return factura;
    }

    public Pedido obtenerPedido(String pedidoId) {
        log.fine("Buscando pedido con ID: " + pedidoId);

        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });
    }

    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        log.fine("Buscando pedidos con estado: " + estado);

        List<Pedido> pedidos = pedidoRepository.findByEstado(estado);
        log.fine("Se encontraron " + pedidos.size() + " pedidos con estado " + estado);

        return pedidos;
    }

    @Transactional
    public Pedido actualizarEstado(String pedidoId, String nuevoEstado) {
        log.info("Actualizando estado del pedido " + pedidoId + " a " + nuevoEstado);

        Pedido pedido = obtenerPedido(pedidoId);
        pedido.setEstado(nuevoEstado);

        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        log.info("Estado actualizado exitosamente");

        return pedidoActualizado;
    }

    public List<Pedido> obtenerPedidosPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.fine("Buscando pedidos entre " + inicio + " y " + fin);

        return pedidoRepository.findPedidosPorFecha(inicio, fin);
    }
}
