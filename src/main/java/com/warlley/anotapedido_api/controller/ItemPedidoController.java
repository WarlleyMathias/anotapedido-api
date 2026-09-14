package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.service.ItemPedidoSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemPedidoController {

    private final ItemPedidoSevice itemPedidoSevice;

    public ItemPedido buscarItemPedido(Long idItempedido){
        return itemPedidoSevice.buscarItemPedido(idItempedido);
    }

    public ItemPedido salvarItemPedido(ItemPedido itemPedido){
        return itemPedidoSevice.salvarItemPedido(itemPedido);
    }

    public ItemPedido editarItemPedido(ItemPedido itemPedido){
        return itemPedidoSevice.editarItemPedido(itemPedido);
    }

    public void removeItemPedido(Long idItemPedido){
        itemPedidoSevice.removeItemPedido(idItemPedido);
    }
}
