package com.delivery.delivery_app.repository;

import com.delivery.delivery_app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    Optional<Usuario> findByNombre(String nombre);
    
    Optional<Usuario> findByTelefono(String telefono);
    
    @Query("SELECT u FROM Usuario u WHERE TYPE(u) = :tipo")
    List<Usuario> findByTipo(@Param("tipo") Class<?> tipo);
    
    @Query("SELECT u FROM Usuario u WHERE u.direccion LIKE %:direccion%")
    List<Usuario> buscarPorDireccion(@Param("direccion") String direccion);
}
