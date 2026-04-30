package com.tienda.monolito.services.interfaces;

import com.tienda.monolito.entities.Billetera;
import com.tienda.monolito.entities.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface BilleteraService {

    Billetera getOrCreateBilletera(Long usuarioId);

    Billetera getBilletera(Long usuarioId);

    Billetera addFunds(Long usuarioId, BigDecimal monto);

    List<Transaccion> getTransacciones(Long usuarioId);
}
