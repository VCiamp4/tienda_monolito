package com.tienda.monolito.services.interfaces;

import com.tienda.monolito.entities.DeliveryOrder;

import java.util.List;

public interface DeliveryService {

    List<DeliveryOrder> findPendientes();

    List<DeliveryOrder> findAsignadosPorEntregador(Long usuarioEntregadorId);

    DeliveryOrder findById(Long id);

    DeliveryOrder asignarEntrega(Long deliveryOrderId, Long usuarioEntregadorId);

    DeliveryOrder marcarEntregado(Long deliveryOrderId);
}
