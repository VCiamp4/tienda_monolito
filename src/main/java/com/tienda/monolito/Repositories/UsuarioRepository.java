package com.tienda.monolito.repositories;

import com.tienda.monolito.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByFirebaseUid(String firebaseUid);

    boolean existsByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);

    List<Usuario> findByPersonaId(Long personaId);

    List<Usuario> findByActivoTrue();
}
