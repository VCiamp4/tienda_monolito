package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByCarritoId(Long carritoId);

    Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

    // Vaciar carrito: desvincula los items sin eliminarlos (DeliveryOrder los referencia)
    @Modifying
    @Query("UPDATE CarritoItem c SET c.carrito = null WHERE c.carrito.id = :carritoId")
    void detachFromCarrito(@Param("carritoId") Long carritoId);
}
