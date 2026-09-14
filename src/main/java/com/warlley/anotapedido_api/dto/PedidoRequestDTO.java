package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PedidoRequestDTO(

        @NotBlank(message = "campo usuário não pode ser vazio.")
        Usuario usuario,
        @NotBlank(message = "campo itemPedidoList não pode ser vazio.")
        List<ItemPedido> itemPedidoList

) {}
