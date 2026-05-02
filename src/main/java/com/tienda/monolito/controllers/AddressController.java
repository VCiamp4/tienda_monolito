package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Direccion;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.DireccionService;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class AddressController {

    private final DireccionService direccionService;
    private final UsuarioService usuarioService;

    @GetMapping("/mine")
    public List<Direccion> getMyDirecciones(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return direccionService.findByPersonaId(usuario.getPersona().getId());
    }
    @GetMapping("/{id}")
    public Direccion getById(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = getUsuario(token);

        Direccion direccion = direccionService.findById(id);

        if (!direccion.getPersona().getId().equals(usuario.getPersona().getId())) {
            throw new RuntimeException("No tienes permiso para acceder a esta dirección");
        }

        return direccion;
    }

    @PostMapping
    public Direccion create(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestBody Direccion direccion
    ) {
        Usuario usuario = getUsuario(token);

        return direccionService.create(
                usuario.getPersona().getId(),
                direccion
        );
    }

    @PutMapping("/{id}")
    public Direccion update(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token,
            @RequestBody Direccion data
    ) {
        Usuario usuario = getUsuario(token);

        Direccion existing = direccionService.findById(id);

        if (!existing.getPersona().getId().equals(usuario.getPersona().getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta dirección");
        }

        return direccionService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void deactivate(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = getUsuario(token);

        Direccion existing = direccionService.findById(id);

        if (!existing.getPersona().getId().equals(usuario.getPersona().getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta dirección");
        }

        direccionService.deactivate(id);
    }

    private Usuario getUsuario(FirebaseToken token) {
        return usuarioService.findByFirebaseUid(token.getUid());
    }
}