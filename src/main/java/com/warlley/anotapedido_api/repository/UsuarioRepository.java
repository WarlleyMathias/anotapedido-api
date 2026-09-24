package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Object> findByEmail(String email);

    boolean existsByEmail(@Email @NotBlank(message = "O campo email não pode está vazio.") String email);
}
