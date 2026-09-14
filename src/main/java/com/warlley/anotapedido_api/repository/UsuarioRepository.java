package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
