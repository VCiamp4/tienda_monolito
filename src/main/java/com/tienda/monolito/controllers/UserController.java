package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Usuario getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/email")
    public Usuario getByEmail(@RequestParam String email) {
        return usuarioService.findByEmail(email);
    }

    @GetMapping("/me")
    public Usuario getMe(@AuthenticationPrincipal FirebaseToken token) {
        return usuarioService.findByFirebaseUid(token.getUid());
    }

    @PostMapping
    public Usuario create(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam Long personaId
    ) {
        return usuarioService.create(
                personaId,
                token.getEmail(),
                token.getUid()
        );
    }

    @PostMapping("/{usuarioId}/roles/{rolId}")
    public Usuario assignRole(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        return usuarioService.assignRole(usuarioId, rolId);
    }

    @DeleteMapping("/{usuarioId}/roles/{rolId}")
    public Usuario removeRole(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId
    ) {
        return usuarioService.removeRole(usuarioId, rolId);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        usuarioService.deactivate(id);
    }
}