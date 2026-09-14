package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Usuario;

public record UsuarioResponseDTO(

        Long id,
        String email,
        String senha,
        String nome,
        String endereco

) {
    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getEmail(), usuario.getSenha(), usuario.getNome(), usuario.getEndereco());
    }
}
