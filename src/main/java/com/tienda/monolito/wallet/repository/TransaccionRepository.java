package com.tienda.monolito.wallet.repository;

import com.tienda.monolito.wallet.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByBilleteraIdOrderByFechaDesc(Long billeteraId);
}
