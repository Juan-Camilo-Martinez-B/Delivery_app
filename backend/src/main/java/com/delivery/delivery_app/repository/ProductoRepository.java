package com.delivery.delivery_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery.delivery_app.model.Producto;
import com.delivery.delivery_app.model.Tienda;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    
    List<Producto> findByCategoria(String categoria);
    
    List<Producto> findByDisponibleTrue();
    
    List<Producto> findByTiendaAndDisponibleTrue(Tienda tienda);
    
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max AND p.disponible = true")
    List<Producto> findProductosEnRangoPrecio(@Param("min") Double min, @Param("max") Double max);
    
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p.categoria, COUNT(p) FROM Producto p GROUP BY p.categoria")
    List<Object[]> contarProductosPorCategoria();
}
