package com.delivery.delivery_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery.delivery_app.model.Tienda;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, String> {
}
