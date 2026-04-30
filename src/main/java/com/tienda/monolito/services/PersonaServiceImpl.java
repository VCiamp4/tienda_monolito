package com.tienda.monolito.services;

import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.Persona;
import com.tienda.monolito.repositories.PersonaRepository;
import com.tienda.monolito.services.interfaces.PersonaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    @Override
    public List<Persona> findAll() {
        return personaRepository.findByActivoTrue();
    }

    @Override
    public Persona findById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + id));
    }

    @Override
    public Persona findByDocumento(String documento) {
        return personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con documento: " + documento));
    }

    @Override
    @Transactional
    public Persona create(Persona persona) {
        if (personaRepository.existsByDocumento(persona.getDocumento())) {
            throw new BusinessException("Ya existe una persona con el documento: " + persona.getDocumento());
        }
        persona.setActivo(true);
        return personaRepository.save(persona);
    }

    @Override
    @Transactional
    public Persona update(Long id, Persona data) {
        Persona persona = findById(id);

        if (!persona.getDocumento().equals(data.getDocumento())
                && personaRepository.existsByDocumento(data.getDocumento())) {
            throw new BusinessException("Ya existe una persona con el documento: " + data.getDocumento());
        }

        persona.setNombreCompleto(data.getNombreCompleto());
        persona.setDocumento(data.getDocumento());
        persona.setTelefono(data.getTelefono());
        persona.setFechaNacimiento(data.getFechaNacimiento());

        return personaRepository.save(persona);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Persona persona = findById(id);
        persona.setActivo(false);
        personaRepository.save(persona);
    }
}
