package com.warlley.anotapedido_api.model;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private Float preco;
    private String imagemUrl;
    @ManyToOne
    @JoinColumn(name = "categoria", nullable = false)
    private Categoria categoria;
    private String descricao;

    public Produto(ProdutoRequestDTO produtoRequestDTO){
        this.nome = produtoRequestDTO.nome();
        this.preco = produtoRequestDTO.preco();
        this.imagemUrl = produtoRequestDTO.imagemUrl();
        this.descricao = produtoRequestDTO.descricao();
    }

    public Produto(ProdutoResponseDTO produtoResponseDTO){
        this.id = produtoResponseDTO.id();
        this.nome = produtoResponseDTO.nome();
        this.preco = produtoResponseDTO.preco();
        this.imagemUrl = produtoResponseDTO.imagemUrl();
        this.categoria = produtoResponseDTO.categoria();
        this.descricao = produtoResponseDTO.descricao();
    }
}
