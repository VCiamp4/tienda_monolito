package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByBilleteraIdOrderByFechaDesc(Long billeteraId);
}
