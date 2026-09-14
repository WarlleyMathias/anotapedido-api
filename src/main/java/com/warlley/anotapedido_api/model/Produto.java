package com.warlley.anotapedido_api.model;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Float valor;

    public Produto(ProdutoRequestDTO produtoRequestDTO){
        this.nome = produtoRequestDTO.nome();
        this.valor = produtoRequestDTO.valor();
    }

    public Produto(ProdutoResponseDTO produtoResponseDTO){
        this.id = produtoResponseDTO.id();
        this.nome = produtoResponseDTO.nome();
        this.valor = produtoResponseDTO.valor();
    }
}
