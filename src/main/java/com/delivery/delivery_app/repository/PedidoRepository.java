package com.delivery.delivery_app.repository;

import com.delivery.delivery_app.model.Pedido;
import com.delivery.delivery_app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    
    List<Pedido> findByUsuario(Usuario usuario);
    
    List<Pedido> findByEstado(String estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.fecha DESC")
    List<Pedido> findHistorialByUsuarioId(@Param("usuarioId") String usuarioId);
    
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado AND p.fecha BETWEEN :inicio AND :fin")
    List<Pedido> findPedidosPorEstadoYFecha(
            @Param("estado") String estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT p FROM Pedido p WHERE p.fecha BETWEEN :inicio AND :fin")
    List<Pedido> findPedidosPorFecha(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") String estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fecha BETWEEN :inicio AND :fin")
    Double sumTotalPedidosEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
