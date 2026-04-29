package com.tienda.monolito.person.repository;

import com.tienda.monolito.person.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Persona> findByActivoTrue();
}
