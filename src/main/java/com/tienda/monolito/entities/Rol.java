package com.tienda.monolito.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
}
