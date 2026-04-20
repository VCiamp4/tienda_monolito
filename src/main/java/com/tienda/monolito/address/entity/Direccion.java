package com.tienda.monolito.address.entity;

import com.tienda.monolito.person.entity.Persona;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direccion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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
