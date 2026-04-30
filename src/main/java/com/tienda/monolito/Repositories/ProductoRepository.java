package com.tienda.monolito.product.repository;

import com.tienda.monolito.product.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    List<Producto> findByUsuarioVendedorIdAndActivoTrue(Long usuarioId);

    // Búsqueda por palabras clave en nombre o descripción (sección 3.3.2)
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Producto> findByKeyword(@Param("keyword") String keyword);

    // Búsqueda combinada por keyword y categoría
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria.id = :categoriaId AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Producto> findByKeywordAndCategoria(@Param("keyword") String keyword,
                                             @Param("categoriaId") Long categoriaId);
}
