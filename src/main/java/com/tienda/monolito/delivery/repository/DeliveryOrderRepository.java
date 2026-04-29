package com.tienda.monolito.delivery.repository;

import com.tienda.monolito.delivery.entity.DeliveryOrder;
import com.tienda.monolito.delivery.entity.EstadoDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    // Pedidos pendientes de asignación (sección 3.6.1)
    List<DeliveryOrder> findByEstado(EstadoDelivery estado);

    // Pedidos asignados a un entregador específico
    List<DeliveryOrder> findByUsuarioEntregadorIdAndEstado(Long usuarioEntregadorId,
                                                           EstadoDelivery estado);

    // Historial de pedidos de un comprador
    List<DeliveryOrder> findByCompradorId(Long compradorId);
}
