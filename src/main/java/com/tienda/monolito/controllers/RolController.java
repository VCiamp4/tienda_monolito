package com.tienda.monolito.controllers;

import com.tienda.monolito.entities.Rol;
import com.tienda.monolito.services.interfaces.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;
    
    @GetMapping
    public List<Rol> getAll() {
        return rolService.findAll();
    }

    @GetMapping("/{id}")
    public Rol getById(@PathVariable Long id) {
        return rolService.findById(id);
    }

    @GetMapping("/nombre")
    public Rol getByNombre(@RequestParam String nombre) {
        return rolService.findByNombre(nombre);
    }
}