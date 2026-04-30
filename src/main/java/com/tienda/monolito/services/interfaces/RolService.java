package com.tienda.monolito.services.interfaces;

import com.tienda.monolito.entities.Rol;

import java.util.List;

public interface RolService {

    List<Rol> findAll();

    Rol findById(Long id);

    Rol findByNombre(String nombre);
}
