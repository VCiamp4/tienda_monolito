package com.tienda.monolito.cart.service;

import com.tienda.monolito.cart.entity.Carrito;
import com.tienda.monolito.cart.entity.CarritoItem;

import java.util.List;

public interface CarritoService {

    Carrito getOrCreateCarrito(Long usuarioId);

    Carrito getCarrito(Long usuarioId);

    List<CarritoItem> getItems(Long usuarioId);

    CarritoItem addItem(Long usuarioId, Long productoId, int cantidad);

    CarritoItem updateItem(Long carritoItemId, int cantidad);

    void removeItem(Long carritoItemId);
}
