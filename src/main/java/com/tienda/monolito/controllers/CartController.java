package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Carrito;
import com.tienda.monolito.entities.CarritoItem;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.CarritoService;
import com.tienda.monolito.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CartController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public Carrito getCarrito(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return carritoService.getOrCreateCarrito(usuario.getId());
    }

    @GetMapping("/items")
    public List<CarritoItem> getItems(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = getUsuario(token);
        return carritoService.getItems(usuario.getId());
    }

    @PostMapping("/items")
    public CarritoItem addItem(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        Usuario usuario = getUsuario(token);
        return carritoService.addItem(usuario.getId(), productoId, cantidad);
    }

    @PutMapping("/items/{itemId}")
    public CarritoItem updateItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam int cantidad
    ) {
        Usuario usuario = getUsuario(token);

        CarritoItem item = getOwnedItem(itemId, usuario);

        return carritoService.updateItem(item.getId(), cantidad);
    }

    @DeleteMapping("/items/{itemId}")
    public void removeItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = getUsuario(token);

        CarritoItem item = getOwnedItem(itemId, usuario);

        carritoService.removeItem(item.getId());
    }

    private Usuario getUsuario(FirebaseToken token) {
        return usuarioService.findByFirebaseUid(token.getUid());
    }

    private CarritoItem getOwnedItem(Long itemId, Usuario usuario) {
        Carrito carrito = carritoService.getCarrito(usuario.getId());

        return carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tenés permiso para acceder a este item"));
    }
}