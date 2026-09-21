package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
boolean existsByEmailFalse(String email);

    Optional<Object> findByEmail(String email);
}
