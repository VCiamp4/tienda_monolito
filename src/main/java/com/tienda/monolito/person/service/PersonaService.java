package com.tienda.monolito.person.service;

import com.tienda.monolito.person.entity.Persona;

import java.util.List;

public interface PersonaService {

    List<Persona> findAll();

    Persona findById(Long id);

    Persona findByDocumento(String documento);

    Persona create(Persona persona);

    Persona update(Long id, Persona data);

    void deactivate(Long id);
}
