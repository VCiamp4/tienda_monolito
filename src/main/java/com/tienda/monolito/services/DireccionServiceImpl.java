package com.tienda.monolito.services;

import com.tienda.monolito.entities.Direccion;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.Persona;
import com.tienda.monolito.repositories.DireccionRepository;
import com.tienda.monolito.repositories.PersonaRepository;
import com.tienda.monolito.services.interfaces.DireccionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final PersonaRepository personaRepository;

    @Override
    public List<Direccion> findByPersonaId(Long personaId) {
        return direccionRepository.findByPersonaIdAndActivoTrue(personaId);
    }

    @Override
    public Direccion findById(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public Direccion create(Long personaId, Direccion direccion) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + personaId));
        direccion.setPersona(persona);
        direccion.setActivo(true);
        return direccionRepository.save(direccion);
    }

    @Override
    @Transactional
    public Direccion update(Long id, Direccion data) {
        Direccion direccion = findById(id);
        direccion.setCalle(data.getCalle());
        direccion.setNumero(data.getNumero());
        direccion.setCiudad(data.getCiudad());
        direccion.setProvincia(data.getProvincia());
        return direccionRepository.save(direccion);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Direccion direccion = findById(id);
        direccion.setActivo(false);
        direccionRepository.save(direccion);
    }
}
