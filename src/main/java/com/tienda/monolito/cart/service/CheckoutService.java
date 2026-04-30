package com.tienda.monolito.cart.service;

public interface CheckoutService {

    void checkout(Long usuarioId, Long direccionEntregaId);
}
