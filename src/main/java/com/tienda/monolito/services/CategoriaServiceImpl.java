package com.tienda.monolito.services;

import com.tienda.monolito.entities.Categoria;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.repositories.CategoriaRepository;
import com.tienda.monolito.services.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }
}
