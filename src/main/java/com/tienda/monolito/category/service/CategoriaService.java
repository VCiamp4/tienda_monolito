package com.tienda.monolito.category.service;

import com.tienda.monolito.category.entity.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAll();

    Categoria findById(Long id);
}
