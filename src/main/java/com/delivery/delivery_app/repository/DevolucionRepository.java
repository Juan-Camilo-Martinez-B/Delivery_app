package com.delivery.delivery_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery.delivery_app.model.Devolucion;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, String> {}
