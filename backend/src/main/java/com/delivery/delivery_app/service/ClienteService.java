package com.delivery.delivery_app.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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
public class ClienteService {

    private static final Logger log = Logger.getLogger(ClienteService.class.getName());

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CalculadoraPedido calculadoraPedido;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodosLosClientes() {
        log.info("Listando todos los clientes");
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        log.info("Creando nuevo cliente: " + cliente.getNombre());
        
        if (cliente.getId() == null) {
            cliente.setId(UUID.randomUUID().toString());
        }
        
        // Validar que no exista cliente con el mismo teléfono
        clienteRepository.findByTelefono(cliente.getTelefono())
                .ifPresent(u -> {
                    throw new RuntimeException("Ya existe un cliente con el teléfono: " + cliente.getTelefono());
                });
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: " + clienteGuardado.getId());
        
        return clienteGuardado;
    }

    public Cliente obtenerClientePorId(String clienteId) {
        log.fine("Buscando cliente con ID: " + clienteId);
        
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.severe("Cliente no encontrado con ID: " + clienteId);
                    return new RuntimeException("Cliente no encontrado con ID: " + clienteId);
                });
    }

    @Transactional
    public Pedido realizarPedido(String clienteId, List<ItemPedidoRequest> items) {
        log.info("Realizando pedido para cliente ID: " + clienteId);
        
        Cliente cliente = obtenerClientePorId(clienteId);
        
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

        // Calcular total usando la calculadora
        Double total = calculadoraPedido.calcularTotal(pedido);
        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("Pedido realizado exitosamente. ID: " + pedidoGuardado.getId() + ", Total: " + total);

        return pedidoGuardado;
    }

    public List<Pedido> verHistorialPedidos(String clienteId) {
        log.fine("Obteniendo historial de pedidos para cliente ID: " + clienteId);
        
        obtenerClientePorId(clienteId);
        List<Pedido> historial = pedidoRepository.findHistorialByClienteId(clienteId);
        
        log.fine("Se encontraron " + historial.size() + " pedidos en el historial");
        
        return historial;
    }

    public List<Producto> buscarProductosPorCategoria(String categoria) {
        log.fine("Buscando productos por categoría: " + categoria);
        
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        log.fine("Se encontraron " + productos.size() + " productos en la categoría " + categoria);
        
        return productos;
    }

    public List<Producto> buscarProductosDisponibles() {
        log.fine("Obteniendo productos disponibles");
        
        List<Producto> productos = productoRepository.findByDisponibleTrue();
        log.fine("Se encontraron " + productos.size() + " productos disponibles");
        
        return productos;
    }

    public List<Producto> obtenerTodosLosProductos() {
        log.fine("Obteniendo todos los productos");
        
        List<Producto> productos = productoRepository.findAll();
        log.fine("Total de productos: " + productos.size());
        
        return productos;
    }

    public Producto obtenerProductoPorId(String productoId) {
        log.fine("Buscando producto con ID: " + productoId);
        
        return productoRepository.findById(productoId)
                .orElseThrow(() -> {
                    log.severe("Producto no encontrado con ID: " + productoId);
                    return new RuntimeException("Producto no encontrado con ID: " + productoId);
                });
    }

    public Pedido obtenerPedidoPorId(String pedidoId) {
        log.fine("Buscando pedido con ID: " + pedidoId);
        
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.severe("Pedido no encontrado con ID: " + pedidoId);
                    return new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                });
    }

    @Transactional
    public Pedido actualizarEstadoPedido(String pedidoId, String nuevoEstado) {
        log.info("Actualizando estado del pedido " + pedidoId + " a " + nuevoEstado);
        
        Pedido pedido = obtenerPedidoPorId(pedidoId);
        pedido.setEstado(nuevoEstado);
        
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        log.info("Estado del pedido actualizado exitosamente");
        
        return pedidoActualizado;
    }
}
