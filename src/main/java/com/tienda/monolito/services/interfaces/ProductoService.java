package com.tienda.monolito.services.interfaces;

import com.tienda.monolito.entities.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> findAllActive();

    Producto findById(Long id);

    List<Producto> findByKeyword(String keyword);

    List<Producto> findByCategoria(Long categoriaId);

    List<Producto> findByVendedor(Long usuarioId);

    List<Producto> findByKeywordAndCategoria(String keyword, Long categoriaId);

    Producto create(Long usuarioVendedorId, Long categoriaId, Long direccionId, Producto producto);

    Producto update(Long id, Producto data);

    void deactivate(Long id);
}
