package com.tienda.monolito.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "billetera")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Billetera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @OneToMany(mappedBy = "billetera", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Transaccion> transacciones;
}
