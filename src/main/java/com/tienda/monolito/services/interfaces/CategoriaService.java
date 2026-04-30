package com.tienda.monolito.services.interfaces;

import com.tienda.monolito.entities.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAll();

    Categoria findById(Long id);
}
