package com.tienda.monolito.address.service;

import com.tienda.monolito.address.entity.Direccion;

import java.util.List;

public interface DireccionService {

    List<Direccion> findByPersonaId(Long personaId);

    Direccion findById(Long id);

    Direccion create(Long personaId, Direccion direccion);

    Direccion update(Long id, Direccion data);

    void deactivate(Long id);
}
