package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Endereco;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.model.enums.UserRole;

import java.util.List;

public record UsuarioResponseDTO(

        Long id,
        String email,
        String senha,
        String nome,
        List<Endereco> enderecos,
        UserRole role

) {
    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getEmail(), usuario.getSenha(), usuario.getNome(), usuario.getEnderecos(), usuario.getRole());
    }
}
