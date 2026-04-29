package com.tienda.monolito.role.service;

import com.tienda.monolito.role.entity.Rol;

import java.util.List;

public interface RolService {

    List<Rol> findAll();

    Rol findById(Long id);

    Rol findByNombre(String nombre);
}
