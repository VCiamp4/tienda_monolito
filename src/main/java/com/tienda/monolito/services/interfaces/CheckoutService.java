package com.tienda.monolito.services.interfaces;

public interface CheckoutService {

    void checkout(Long usuarioId, Long direccionEntregaId);
}
