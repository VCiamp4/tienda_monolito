package com.tienda.monolito.controllers;

import com.tienda.monolito.entities.Persona;
import com.tienda.monolito.services.interfaces.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonController {

    private final PersonaService personaService;

    @GetMapping
    public List<Persona> getAll() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public Persona getById(@PathVariable Long id) {
        return personaService.findById(id);
    }

    @GetMapping("/documento")
    public Persona getByDocumento(@RequestParam String documento) {
        return personaService.findByDocumento(documento);
    }

    @PostMapping
    public Persona create(@RequestBody Persona persona) {
        return personaService.create(persona);
    }

    @PutMapping("/{id}")
    public Persona update(
            @PathVariable Long id,
            @RequestBody Persona persona
    ) {
        return personaService.update(id, persona);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        personaService.deactivate(id);
    }
}