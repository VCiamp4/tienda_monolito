package com.tienda.monolito.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.tienda.monolito.entities.Usuario;

public class UserTest {
  
    @Test
    public void testUsuarioCreation() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("john.doe@example.com")
                .firebaseUid("firebase_uid_123")
                .activo(true)
                .build();

        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(usuario.getFirebaseUid()).isEqualTo("firebase_uid_123");
        assertThat(usuario.getActivo()).isTrue();
    }
}
