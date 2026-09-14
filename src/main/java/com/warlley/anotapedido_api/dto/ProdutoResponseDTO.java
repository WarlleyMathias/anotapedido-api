package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Produto;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Float valor
) {
    public ProdutoResponseDTO(Produto produto){
        this(produto.getId(), produto.getNome(), produto.getValor());
    }
}
