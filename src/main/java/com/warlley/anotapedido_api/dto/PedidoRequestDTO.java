package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PedidoRequestDTO(

        @NotBlank(message = "campo usuário não pode ser vazio.")
        Long usuarioId,
        @NotBlank(message = "campo itemPedidoList não pode ser vazio.")
        List<ItemPedidoRequestDTO> itemPedidoRequestDTOList

) {}
