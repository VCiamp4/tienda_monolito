package com.tienda.monolito.delivery.entity;

import com.tienda.monolito.address.entity.Direccion;
import com.tienda.monolito.cart.entity.CarritoItem;
import com.tienda.monolito.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_order")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_item_id", nullable = false)
    private CarritoItem carritoItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprador_id", nullable = false)
    private Usuario comprador;

    // nullable: se asigna cuando un entregador toma el pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_entregador_id")
    private Usuario usuarioEntregador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_entrega_id", nullable = false)
    private Direccion direccionEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDelivery estado;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
