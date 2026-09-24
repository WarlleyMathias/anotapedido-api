package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.model.enums.UserRole;

public record UsuarioResponseDTO(

        Long id,
        String email,
        String senha,
        String nome,
        String endereco,
        UserRole role

) {
    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getEmail(), usuario.getSenha(), usuario.getNome(), usuario.getEndereco(), usuario.getRole());
    }
}
