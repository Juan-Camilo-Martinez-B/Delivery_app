package com.delivery.delivery_app.config;

import java.time.LocalTime;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.delivery_app.model.Cliente;
import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Repartidor;
import com.delivery.delivery_app.model.Tienda;
import com.delivery.delivery_app.repository.ProductoRepository;
import com.delivery.delivery_app.repository.UsuarioRepository;
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

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Cargando datos iniciales...");
        
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
    }
}
