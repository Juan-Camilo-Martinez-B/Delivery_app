package com.delivery.delivery_app.repository;

import com.delivery.delivery_app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByNombre(String nombre);
    Optional<Cliente> findByTelefono(String telefono);
}