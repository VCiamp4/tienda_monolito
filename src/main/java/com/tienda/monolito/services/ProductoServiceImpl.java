package com.tienda.monolito.services;

import com.tienda.monolito.entities.Direccion;
import com.tienda.monolito.entities.Categoria;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.Producto;
import com.tienda.monolito.repositories.CategoriaRepository;
import com.tienda.monolito.repositories.ProductoRepository;
import com.tienda.monolito.repositories.DireccionRepository;
import com.tienda.monolito.repositories.UsuarioRepository;
import com.tienda.monolito.services.interfaces.ProductoService;
import com.tienda.monolito.entities.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final DireccionRepository direccionRepository;

    @Override
    public List<Producto> findAllActive() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    public List<Producto> findByKeyword(String keyword) {
        return productoRepository.findByKeyword(keyword);
    }

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    @Override
    public List<Producto> findByVendedor(Long usuarioId) {
        return productoRepository.findByUsuarioVendedorIdAndActivoTrue(usuarioId);
    }

    @Override
    @Transactional
    public Producto create(Long usuarioVendedorId, Long categoriaId, Long direccionId, Producto producto) {
        Usuario vendedor = usuarioRepository.findById(usuarioVendedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioVendedorId));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + categoriaId));
        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada con id: " + direccionId));

        producto.setUsuarioVendedor(vendedor);
        producto.setCategoria(categoria);
        producto.setDireccion(direccion);
        producto.setActivo(true);

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto update(Long id, Producto data) {
        Producto producto = findById(id);

        producto.setNombre(data.getNombre());
        producto.setDescripcion(data.getDescripcion());
        producto.setPrecio(data.getPrecio());
        producto.setStock(data.getStock());
        producto.setImageUrl(data.getImageUrl());

        if (data.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(data.getCategoria().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        if (data.getDireccion() != null) {
            Direccion direccion = direccionRepository.findById(data.getDireccion().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));
            producto.setDireccion(direccion);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Producto producto = findById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
