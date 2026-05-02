package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Billetera;
import com.tienda.monolito.entities.Transaccion;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.BilleteraService;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/billetera")
@RequiredArgsConstructor
public class WalletController {

    private final BilleteraService billeteraService;
    private final UsuarioService usuarioService;

    @GetMapping
    public Billetera getBilletera(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return billeteraService.getOrCreateBilletera(usuario.getId());
    }

    @PostMapping("/cargar")
    public Billetera addFunds(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam BigDecimal monto
    ) {
        Usuario usuario = getUsuario(token);
        return billeteraService.addFunds(usuario.getId(), monto);
    }

    @GetMapping("/transacciones")
    public List<Transaccion> getTransacciones(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return billeteraService.getTransacciones(usuario.getId());
    }

    private Usuario getUsuario(FirebaseToken token) {
        return usuarioService.findByFirebaseUid(token.getUid());
    }
}