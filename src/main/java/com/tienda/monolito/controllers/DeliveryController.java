package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.DeliveryOrder;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.DeliveryService;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UsuarioService usuarioService;

    @GetMapping("/pendientes")
    public List<DeliveryOrder> getPendientes() {
        return deliveryService.findPendientes();
    }

    @GetMapping("/mine")
    public List<DeliveryOrder> getMyAssigned(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return deliveryService.findAsignadosPorEntregador(usuario.getId());
    }

    @GetMapping("/{id}")
    public DeliveryOrder getById(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {

        DeliveryOrder order = deliveryService.findById(id);
        return order;
    }

    @PostMapping("/{id}/asignar")
    public DeliveryOrder asignar(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = getUsuario(token);

        return deliveryService.asignarEntrega(id, usuario.getId());
    }

    @PostMapping("/{id}/entregar")
    public DeliveryOrder marcarEntregado(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = getUsuario(token);

        DeliveryOrder order = deliveryService.findById(id);

        if (order.getUsuarioEntregador() == null ||
            !order.getUsuarioEntregador().getId().equals(usuario.getId())) {
            throw new RuntimeException("No podés marcar como entregada una orden que no te pertenece");
        }

        return deliveryService.marcarEntregado(id);
    }

    private Usuario getUsuario(FirebaseToken token) {
        return usuarioService.findByFirebaseUid(token.getUid());
    }
}