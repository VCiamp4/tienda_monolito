package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.Billetera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BilleteraRepository extends JpaRepository<Billetera, Long> {

    Optional<Billetera> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}
