package com.tienda.monolito.user.service;

import com.tienda.monolito.user.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario findByFirebaseUid(String firebaseUid);

    Usuario findByEmail(String email);

    Usuario create(Long personaId, String email, String firebaseUid);

    void deactivate(Long id);

    Usuario assignRole(Long usuarioId, Long rolId);

    Usuario removeRole(Long usuarioId, Long rolId);

    void updateLastAccess(String firebaseUid);
}
