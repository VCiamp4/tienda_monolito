package com.tienda.monolito.address.repository;

import com.tienda.monolito.address.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    List<Direccion> findByPersonaId(Long personaId);

    List<Direccion> findByPersonaIdAndActivoTrue(Long personaId);
}
