package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Produto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemPedidoRequestDTO(

        @NotBlank(message = "campo produto não pode ser vazio.")
        Produto produto,
        @NotBlank(message = "campo pedido não pode ser vazio.")
        Pedido pedido,
        @Min(value = 1,message = "o campo quntidade não pode ser menor que 1.")
        @NotBlank(message = "o campo quantidade não pode ser vazio")
        int quantidade

) {}
