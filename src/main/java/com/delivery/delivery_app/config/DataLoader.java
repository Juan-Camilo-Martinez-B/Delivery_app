package com.delivery.delivery_app.config;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.dto.ItemPedidoRequest;
import com.delivery.delivery_app.dto.PagoRequest;
import com.delivery.delivery_app.envio.Envio;
import com.delivery.delivery_app.envio.EnvioEstandar;
import com.delivery.delivery_app.envio.EnvioExpres;
import com.delivery.delivery_app.envio.EnvioInternacional;
import com.delivery.delivery_app.envio.EnvioPorDron;
import com.delivery.delivery_app.model.Cliente;
import com.delivery.delivery_app.model.Pago;
import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Repartidor;
import com.delivery.delivery_app.model.Tienda;
import com.delivery.delivery_app.repository.ProductoRepository;
import com.delivery.delivery_app.repository.UsuarioRepository;
import com.delivery.delivery_app.service.ClienteService;
import com.delivery.delivery_app.service.PagoService;
import com.delivery.delivery_app.service.PedidoService;
import com.delivery.delivery_app.service.RepartidorService;
import com.delivery.delivery_app.service.TiendaService;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DataLoader.class.getName());

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private TiendaService tiendaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private RepartidorService repartidorService;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Cargando datos iniciales...");
        
        ajustarEsquemaTiendasSiEsNecesario();
        
        if (usuarioRepository.findByTelefono("111").isPresent()
                || usuarioRepository.findByTelefono("123456789").isPresent()
                || usuarioRepository.findByTelefono("987654321").isPresent()) {
            log.info("Datos iniciales ya existen. Saltando DataLoader.");
            return;
        }
        
        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID().toString());
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle Principal 123");
        usuarioRepository.save(cliente);
        log.info("Cliente creado: " + cliente.getNombre() + " - ID: " + cliente.getId());

        // Crear repartidor
        Repartidor repartidor = new Repartidor();
        repartidor.setId(UUID.randomUUID().toString());
        repartidor.setNombre("Carlos López");
        repartidor.setTelefono("987654321");
        repartidor.setDireccion("Avenida Central 456");
        repartidor.setDisponible(true);
        repartidor.setVehiculo("Moto");
        usuarioRepository.save(repartidor);
        log.info("Repartidor creado: " + repartidor.getNombre() + " - ID: " + repartidor.getId());

        // Crear tienda
        Tienda tienda = new Tienda();
        tienda.setId(UUID.randomUUID().toString());
        tienda.setNombre("Super Tienda");
        tienda.setTelefono("5551234");
        tienda.setDireccion("Calle Comercio 789");
        tienda.setHorarioApertura(LocalTime.of(8, 0));
        tienda.setHorarioCierre(LocalTime.of(22, 0));
        tiendaService.crearTienda(tienda);
        log.info("Tienda creada: " + tienda.getNombre() + " - ID: " + tienda.getId());

        // Crear productos
        Producto producto1 = new Producto();
        producto1.setId(UUID.randomUUID().toString());
        producto1.setNombre("Hamburguesa Clásica");
        producto1.setPrecio(12.99);
        producto1.setDisponible(true);
        producto1.setCategoria("Comida Rápida");
        producto1.setTienda(tienda);
        productoRepository.save(producto1);

        Producto producto2 = new Producto();
        producto2.setId(UUID.randomUUID().toString());
        producto2.setNombre("Pizza Familiar");
        producto2.setPrecio(18.50);
        producto2.setDisponible(true);
        producto2.setCategoria("Comida Rápida");
        producto2.setTienda(tienda);
        productoRepository.save(producto2);

        Producto producto3 = new Producto();
        producto3.setId(UUID.randomUUID().toString());
        producto3.setNombre("Refresco 2L");
        producto3.setPrecio(3.50);
        producto3.setDisponible(true);
        producto3.setCategoria("Bebidas");
        producto3.setTienda(tienda);
        productoRepository.save(producto3);

        log.info("Productos creados: " + producto1.getNombre() + ", " + producto2.getNombre() + ", " + producto3.getNombre());
        log.info("=== DATOS INICIALES CARGADOS CORRECTAMENTE ===");
        log.info("Cliente ID: " + cliente.getId());
        log.info("Repartidor ID: " + repartidor.getId());
        log.info("Tienda ID: " + tienda.getId());
        log.info("Producto IDs: " + producto1.getId() + ", " + producto2.getId() + ", " + producto3.getId());
        
        log.info("=== SIMULACIÓN DE FLUJO COMPLETO ===");
        
        List<Producto> productosDisponibles = productoRepository.findAll();
        log.info("Productos disponibles: " + productosDisponibles.size());
        for (Producto p : productosDisponibles) {
            log.info("Producto -> ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Precio: " + p.getPrecio());
        }
        
        Cliente ana = new Cliente();
        ana.setId(UUID.randomUUID().toString());
        ana.setNombre("Ana");
        ana.setTelefono("111");
        ana.setDireccion("Calle 1");
        ana = (Cliente) clienteService.crearUsuario(ana);
        log.info("Cliente simulado creado: " + ana.getNombre() + " - ID: " + ana.getId());
        
        Producto productoPedido1 = productosDisponibles.get(0);
        Producto productoPedido2 = productosDisponibles.size() > 1 ? productosDisponibles.get(1) : productoPedido1;
        
        List<ItemPedidoRequest> items = Arrays.asList(
                new ItemPedidoRequest(productoPedido1.getId(), 1, productoPedido1.getPrecio()),
                new ItemPedidoRequest(productoPedido2.getId(), 2, productoPedido2.getPrecio())
        );
        
        Pedido pedido = pedidoService.crearPedido(ana.getId(), items);
        log.info("Pedido creado: " + pedido.getId() + " - Estado: " + pedido.getEstado() + " - Total: " + pedido.getTotal());
        
        String factura = pedidoService.generarFactura(pedido.getId());
        log.info("Factura generada:\n" + factura);
        
        PagoRequest pagoRequest = new PagoRequest(pedido.getId(), pedido.getTotal() + 10.0, "EFECTIVO");
        pagoRequest.setReferencia("SIM-REF-1");
        Pago pago = pagoService.procesarPago(pagoRequest);
        log.info("Pago procesado: " + pago.getId() + " - Completado: " + pago.getCompletado() + " - Monto: " + pago.getMonto());
        
        Pedido pedidoListo = tiendaService.marcarPedidoListo(pedido.getId());
        log.info("Pedido marcado como listo: " + pedidoListo.getId() + " - Estado: " + pedidoListo.getEstado());
        
        Pedido pedidoEnCamino = repartidorService.aceptarPedido(pedido.getId());
        log.info("Pedido aceptado por repartidor: " + pedidoEnCamino.getId() + " - Estado: " + pedidoEnCamino.getEstado());
        
        Pedido pedidoEntregado = repartidorService.marcarComoEntregado(pedido.getId());
        log.info("Pedido entregado: " + pedidoEntregado.getId() + " - Estado: " + pedidoEntregado.getEstado());
        
        Pago pagoReembolsado = pagoService.reembolsarPago(pago.getId());
        log.info("Reembolso procesado: " + pagoReembolsado.getId() + " - Completado: " + pagoReembolsado.getCompletado());
        
        List<Envio> envios = Arrays.asList(
                new EnvioEstandar(3.2, 2.0),
                new EnvioExpres(1.0, 4.5),
                new EnvioInternacional(10.0, 7.0),
                new EnvioPorDron(0.8, 0.6)
        );
        
        for (Envio envio : envios) {
            double costo = envio.calcularCosto();
            log.info("Simulación envío -> " + envio.getClass().getSimpleName() + ": costo=" + costo +
                    ", peso=" + envio.getPeso() + ", volumen=" + envio.getVolumen());
        }
        
        log.info("=== FIN SIMULACIÓN ===");
    }
    
    private void ajustarEsquemaTiendasSiEsNecesario() {
        try {
            entityManager.createNativeQuery(
                    "INSERT INTO usuarios (id, nombre, telefono, direccion) " +
                    "SELECT t.id, COALESCE(t.nombre, 'Tienda'), t.telefono, t.direccion " +
                    "FROM tiendas t " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuarios u WHERE u.id = t.id)"
            ).executeUpdate();
        } catch (Exception ignored) {
        }
        try {
            entityManager.createNativeQuery("ALTER TABLE tiendas ALTER COLUMN nombre DROP NOT NULL").executeUpdate();
        } catch (Exception ignored) {
        }
        try {
            entityManager.createNativeQuery("ALTER TABLE tiendas ALTER COLUMN telefono DROP NOT NULL").executeUpdate();
        } catch (Exception ignored) {
        }
        try {
            entityManager.createNativeQuery("ALTER TABLE tiendas ALTER COLUMN direccion DROP NOT NULL").executeUpdate();
        } catch (Exception ignored) {
        }
    }
}
