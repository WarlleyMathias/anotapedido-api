package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(

        @Size(min = 2, message = "nome não pode ser menor que 2 letras.")
        @NotBlank(message = "campo nome não pode ser vazio.")
        String nome,
        @NotBlank(message = "campo valor não pode ser vazio.")
        Float preco,
        @Size(min = 2, message = "imagemUrl não pode ser menor que 2 letras.")
        @NotBlank(message = "campo imagemUrl não pode ser vazio.")
        String imagemUrl,
        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId,
        @NotBlank(message = "campo nome não pode ser vazio.")
        String descricao

) {}
