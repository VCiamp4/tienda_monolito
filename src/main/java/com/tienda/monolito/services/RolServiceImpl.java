package com.tienda.monolito.services;

import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.repositories.RolRepository;
import com.tienda.monolito.entities.Rol;
import com.tienda.monolito.services.interfaces.RolService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombre));
    }
}
