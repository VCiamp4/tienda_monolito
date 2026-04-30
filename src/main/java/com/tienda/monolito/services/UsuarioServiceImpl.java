package com.tienda.monolito.services;

import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.Persona;
import com.tienda.monolito.repositories.PersonaRepository;
import com.tienda.monolito.repositories.RolRepository;
import com.tienda.monolito.repositories.UsuarioRepository;
import com.tienda.monolito.entities.Rol;
import com.tienda.monolito.services.interfaces.UsuarioService;
import com.tienda.monolito.entities.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario findByFirebaseUid(String firebaseUid) {
        return usuarioRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con UID: " + firebaseUid));
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional
    public Usuario create(Long personaId, String email, String firebaseUid) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Ya existe un usuario con el email: " + email);
        }
        if (usuarioRepository.existsByFirebaseUid(firebaseUid)) {
            throw new BusinessException("Ya existe un usuario con ese UID de Firebase");
        }

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + personaId));

        Usuario usuario = Usuario.builder()
                .persona(persona)
                .email(email)
                .firebaseUid(firebaseUid)
                .activo(true)
                .roles(new ArrayList<>())
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Usuario usuario = findById(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario assignRole(Long usuarioId, Long rolId) {
        Usuario usuario = findById(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + rolId));

        if (!usuario.getRoles().contains(rol)) {
            usuario.getRoles().add(rol);
            usuarioRepository.save(usuario);
        }
        return usuario;
    }

    @Override
    @Transactional
    public Usuario removeRole(Long usuarioId, Long rolId) {
        Usuario usuario = findById(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + rolId));

        usuario.getRoles().remove(rol);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void updateLastAccess(String firebaseUid) {
        usuarioRepository.findByFirebaseUid(firebaseUid).ifPresent(usuario -> {
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }
}
