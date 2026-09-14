package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Usuario;

import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Usuario usuario,
        List<ItemPedido> itemPedidoList
) {
    public PedidoResponseDTO(Pedido pedido){
        this(pedido.getId(), pedido.getUsuario(),pedido.getItemPedidoList());
    }
}
