package com.tienda.monolito.wallet.service;

import com.tienda.monolito.wallet.entity.Billetera;
import com.tienda.monolito.wallet.entity.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface BilleteraService {

    Billetera getOrCreateBilletera(Long usuarioId);

    Billetera getBilletera(Long usuarioId);

    Billetera addFunds(Long usuarioId, BigDecimal monto);

    List<Transaccion> getTransacciones(Long usuarioId);
}
