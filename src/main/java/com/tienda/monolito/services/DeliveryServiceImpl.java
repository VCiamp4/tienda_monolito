package com.tienda.monolito.services;

import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.DeliveryOrder;
import com.tienda.monolito.entities.EstadoDelivery;
import com.tienda.monolito.repositories.DeliveryOrderRepository;
import com.tienda.monolito.repositories.UsuarioRepository;
import com.tienda.monolito.services.interfaces.DeliveryService;
import com.tienda.monolito.entities.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<DeliveryOrder> findPendientes() {
        return deliveryOrderRepository.findByEstado(EstadoDelivery.PENDIENTE);
    }

    @Override
    public List<DeliveryOrder> findAsignadosPorEntregador(Long usuarioEntregadorId) {
        return deliveryOrderRepository.findByUsuarioEntregadorIdAndEstado(
                usuarioEntregadorId, EstadoDelivery.ASIGNADO);
    }

    @Override
    public DeliveryOrder findById(Long id) {
        return deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery order no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public DeliveryOrder asignarEntrega(Long deliveryOrderId, Long usuarioEntregadorId) {
        DeliveryOrder order = findById(deliveryOrderId);

        if (order.getEstado() != EstadoDelivery.PENDIENTE) {
            throw new BusinessException("Solo se pueden asignar entregas en estado PENDIENTE");
        }

        Usuario entregador = usuarioRepository.findById(usuarioEntregadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioEntregadorId));

        order.setUsuarioEntregador(entregador);
        order.setEstado(EstadoDelivery.ASIGNADO);
        return deliveryOrderRepository.save(order);
    }

    @Override
    @Transactional
    public DeliveryOrder marcarEntregado(Long deliveryOrderId) {
        DeliveryOrder order = findById(deliveryOrderId);

        if (order.getEstado() != EstadoDelivery.ASIGNADO) {
            throw new BusinessException("Solo se pueden marcar como entregadas las órdenes en estado ASIGNADO");
        }

        order.setEstado(EstadoDelivery.ENTREGADO);
        order.setFechaEntrega(LocalDateTime.now());
        return deliveryOrderRepository.save(order);
    }
}
