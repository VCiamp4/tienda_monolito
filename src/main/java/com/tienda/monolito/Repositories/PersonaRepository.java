package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Persona> findByActivoTrue();
}
