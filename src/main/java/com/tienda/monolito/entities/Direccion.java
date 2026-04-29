package com.tienda.monolito.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direccion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private Boolean activo;
}
