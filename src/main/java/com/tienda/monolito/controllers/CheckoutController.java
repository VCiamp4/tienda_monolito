package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Direccion;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.repositories.DireccionRepository;
import com.tienda.monolito.services.interfaces.CheckoutService;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final UsuarioService usuarioService;
    private final DireccionRepository direccionRepository;

    @PostMapping
    public void checkout(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam Long direccionEntregaId
    ) {
        Usuario usuario = usuarioService.findByFirebaseUid(token.getUid());

        Direccion direccion = direccionRepository.findById(direccionEntregaId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getPersona().getId().equals(usuario.getPersona().getId())) {
            throw new RuntimeException("No puedes usar una dirección que no te pertenece");
        }

        checkoutService.checkout(usuario.getId(), direccionEntregaId);
    }
}