package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Categoria;
import com.warlley.anotapedido_api.model.Produto;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Float preco,
        String imagemUrl,
        Categoria categoria,
        String descricao
) {
    public ProdutoResponseDTO(Produto produto){
        this(produto.getId(), produto.getNome(), produto.getPreco(), produto.getImagemUrl(), produto.getCategoria(), produto.getDescricao());
    }
}
