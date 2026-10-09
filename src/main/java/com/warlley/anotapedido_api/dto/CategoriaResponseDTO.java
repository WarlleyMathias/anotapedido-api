package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome
){
    public CategoriaResponseDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}
