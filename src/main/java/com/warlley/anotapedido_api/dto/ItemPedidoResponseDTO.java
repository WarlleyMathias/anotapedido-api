package com.warlley.anotapedido_api.dto;

import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Produto;

public record ItemPedidoResponseDTO(

        Long id,
        Produto produto,
        int quantidade,
        Float precoUnitario,
        Float precoSubTotal,
        Pedido pedido
) {
    public ItemPedidoResponseDTO(ItemPedido itemPedido){
        this(itemPedido.getId(), itemPedido.getProduto(), itemPedido.getQuantidade(), itemPedido.getPrecoUnitario(), itemPedido.getPrecoSubTotal(), itemPedido.getPedido());
    }
}
