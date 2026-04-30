package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}
