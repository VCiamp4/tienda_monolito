package com.tienda.monolito.controllers;

import com.google.firebase.auth.FirebaseToken;
import com.tienda.monolito.entities.Producto;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.services.interfaces.ProductoService;
import com.tienda.monolito.services.interfaces.UsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<Producto> getAllActive() {
        return productoService.findAllActive();
    }

    @GetMapping("/{id}")
    public Producto getById(@PathVariable Long id) {
        return productoService.findById(id);
    }

    @GetMapping("/search")
    public List<Producto> search(@RequestParam String keyword) {
        return productoService.findByKeyword(keyword);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> getByCategoria(@PathVariable Long categoriaId) {
        return productoService.findByCategoria(categoriaId);
    }

    @GetMapping("/mine")
    public List<Producto> getMyProducts(@AuthenticationPrincipal FirebaseToken token) {
        Usuario usuario = usuarioService.findByFirebaseUid(token.getUid());
        return productoService.findByVendedor(usuario.getId());
    }

    @GetMapping("/search/filter")
    public List<Producto> searchByKeywordAndCategoria(
            @RequestParam String keyword,
            @RequestParam Long categoriaId
    ) {
        return productoService.findByKeywordAndCategoria(keyword, categoriaId);
    }

    @PostMapping
    public Producto create(
            @AuthenticationPrincipal FirebaseToken token,
            @RequestParam Long categoriaId,
            @RequestParam Long direccionId,
            @RequestBody Producto producto
    ) {
        Usuario usuario = usuarioService.findByFirebaseUid(token.getUid());

        return productoService.create(
                usuario.getId(),
                categoriaId,
                direccionId,
                producto
        );
    }

    @PutMapping("/{id}")
    public Producto update(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token,
            @RequestBody Producto data
    ) {
        Usuario usuario = usuarioService.findByFirebaseUid(token.getUid());

        Producto existing = productoService.findById(id);

        if (!existing.getUsuarioVendedor().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tenés permiso para modificar este producto");
        }

        return productoService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void deactivate(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebaseToken token
    ) {
        Usuario usuario = usuarioService.findByFirebaseUid(token.getUid());

        Producto existing = productoService.findById(id);

        if (!existing.getUsuarioVendedor().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tenés permiso para eliminar este producto");
        }

        productoService.deactivate(id);
    }

}