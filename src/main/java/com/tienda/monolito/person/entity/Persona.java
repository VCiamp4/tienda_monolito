package com.tienda.monolito.person.entity;

import com.tienda.monolito.address.entity.Direccion;
import com.tienda.monolito.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "persona")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String documento;

    private String telefono;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDate fechaRegistro;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Direccion> direcciones;
}
