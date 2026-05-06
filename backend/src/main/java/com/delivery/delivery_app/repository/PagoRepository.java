package com.delivery.delivery_app.repository;

import com.delivery.delivery_app.model.Pago;
import com.delivery.delivery_app.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    
    List<Pago> findByPedido(Pedido pedido);
    
    List<Pago> findByMetodo(String metodo);
    
    @Query("SELECT p FROM Pago p WHERE p.completado = true AND p.fecha BETWEEN :inicio AND :fin")
    List<Pago> findPagosCompletadosEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.completado = true AND p.fecha BETWEEN :inicio AND :fin")
    Double sumMontoPagosCompletadosEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT p.metodo, COUNT(p), SUM(p.monto) FROM Pago p WHERE p.completado = true GROUP BY p.metodo")
    List<Object[]> estadisticasPorMetodoPago();
}
